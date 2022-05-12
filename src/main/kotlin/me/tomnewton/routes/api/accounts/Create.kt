package me.tomnewton.routes.api.accounts

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.date.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.database.model.Account
import me.tomnewton.plugins.parseObject
import me.tomnewton.routes.api.createTokenFor
import me.tomnewton.shared.responses.EMPTY_RESPONSE
import me.tomnewton.shared.responses.Response
import me.tomnewton.shared.responses.accounts.AccountCreateFailResponse
import me.tomnewton.shared.responses.accounts.AccountCreateSuccessResponse
import java.lang.Character.isLetter
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger

internal const val defaultImage =
    "https://i.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U"

fun Route.createAccount(accountDAO: AccountDAO) {
    post("/create") {
        val content = parseObject(call.receiveText())
        val username = content.getOrDefault("username", null) as String?
        val email = content.getOrDefault("email", null) as String?
        val password = content.getOrDefault("password", null) as String?
        val dateOfBirth = content.getOrDefault("dateOfBirth", null) as String?

        if (setOf(username, email, password, dateOfBirth).size != 4) {
            // One or more of them were null, tell them
            Logger.getGlobal().info("Account create fail - not all information provided")
            call.respondText(
                AccountCreateFailResponse("Must provide a username, email, password and dateOfBirth field").toJsonObject(),
                ContentType.Application.Json,
                HttpStatusCode.BadRequest
            )
            return@post
        }

        // Projects and teams should be empty after creating an account
        val account =
            Account(System.nanoTime(), username!!, email!!, password!!, dateOfBirth!!, listOf(), listOf(), defaultImage)

        val (valid, validateResponse) = validate(account)

        if (valid) {

            // Create a JWT for the client to handle, with a shared secret
            val token = createTokenFor(account.id)

            val insertResponse = insert(account, accountDAO, token)
            Logger.getGlobal().info("Account created successfully - ${account.id}")
            call.respondText(insertResponse.toJsonObject(), ContentType.Application.Json)

        } else {
            Logger.getGlobal().info("Account create fail - invalid account details")
            call.respondText(validateResponse.toJsonObject(), ContentType.Application.Json, HttpStatusCode.BadRequest)
        }
    }
}

private fun validate(account: Account): Pair<Boolean, Response> {
    if (!isValidUsername(account.username)) return false to AccountCreateFailResponse("Username contain only letters, digits and underscores, and be a maximum of 20 characters long")
    if (!isValidEmail(account.email)) return false to AccountCreateFailResponse("Invalid email")
    if (!isValidPassword(account.password)) return false to AccountCreateFailResponse("Password must contain a capital, punctuation, number and be at least 7 characters long")
    if (!isValidDateOfBirth(account.dateOfBirth)) return false to AccountCreateFailResponse("Invalid date of birth. You must be at least 13 years old to create an account")

    return true to EMPTY_RESPONSE
}

private fun insert(account: Account, accountDAO: AccountDAO, token: String): Response {
    val success = accountDAO.insertAccount(account)
    return if (success) AccountCreateSuccessResponse(account.id, token)
    else AccountCreateFailResponse("Unable to create the account. This is a server error - please try again")
}

// USERNAME

fun isValidUsername(username: String): Boolean {
    return usernameIsValidLength(username) && usernameOnlyValidChars(username)
}

// Username in the range of 1 to 20 (both inclusive)
fun usernameIsValidLength(username: String): Boolean {
    return username.length in 1..20
}

// Username doesn't contain any characters that aren't valid
fun usernameOnlyValidChars(username: String): Boolean {
    return !username.any { !isValidUsernameCharacter(it) }
}

// EMAIL

// https://www.w3resource.com/javascript/form/email-validation.php
fun isValidEmail(email: String): Boolean {
    return emailContainsValidUsername(email) && emailContainsValidDomain(email)
}

// Checks that the email contains a valid username before the @
fun emailContainsValidUsername(email: String): Boolean {
    if (email.isEmpty()) return false // Must have a character there
    val domainStart = email.indexOf('@')
    // Get the string from the start to the @, exclusive. If there is no @, go to the end of the string
    val username = email.substring(0, if (domainStart == -1) email.length else domainStart)

    /*
    Matches:
    - The start of a string
    - A large group of characters, followed by a dot, all of this any amount of times (to make sure a dot isn't the first character)
    - The same large group of characters at least once (to ensure the dot wasn't the last character)
    - The end of a string
    */
    return username.matches(Regex("^([-!#\$%&'*/=?^`{|}_\"+A-Za-z0-9]+\\.)*[-!#\$%&'*/=?^`{|}_\"+A-Za-z0-9]+\$"))
}

// Checks that the email contains a valid domain after the @
fun emailContainsValidDomain(email: String): Boolean {
    // this method needs something of the form @x.y to just validate the domain, regardless of any other part
    if (email.length < 4) return false
    val domainStart = email.indexOf('@')
    if (domainStart == -1) return false // No @ was found
    val domain =
        email.substring(domainStart + 1) // Get a new string, starting after the @ (safe since we have >=4 chars)

    /*
    Matches:
    - The start of a string
    - Alphanumeric characters, then optionally a hyphen or dot, all of this any amount of times
    - An alphanumeric character (so it doesn't end with a hyphen or dot, and must be at least 1 character long)
    - A dot
    - 2 or alphanumeric characters
    - The end of a string
     */

    return domain.matches(Regex("^([A-Za-z0-9]+[-.]?)*[A-Za-z0-9]\\.[A-Za-z0-9]{2,}\$"))
}

// PASSWORD

// Password must contain a lowercase, capital, punctuation, number, and be at least 7 characters long
fun isValidPassword(password: String): Boolean {
    return containsLowerCase(password) && containsCapital(password) && containsPunctuation(password) && containsDigit(
        password
    ) && passwordIsValidLength(password)
}

// Password must contain a lowercase letter, which is any letter whose lowercase is itself
fun containsLowerCase(password: String): Boolean {
    return password.any { it.isLowerCase() }
}

// Password must contain a capital letter, which is any letter whose uppercase is itself
fun containsCapital(password: String): Boolean {
    return password.any { it.isUpperCase() }
}

// Password must contain punctuation, which is anything satisfying !isLetter and !isDigit
fun containsPunctuation(password: String): Boolean {
    return password.any { !(isDigit(it) || isLetter(it)) }
}

// Password must contain a digit, which is anything satisfying the isDigit fun
fun containsDigit(password: String): Boolean {
    return password.any(::isDigit)
}

// Password must be at least 7 characters long
fun passwordIsValidLength(password: String): Boolean {
    return password.length >= 7
}

// DATE OF BIRTH

fun isValidDateOfBirth(dateOfBirth: String): Boolean {
    val date = SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirth.replace('/', '-'))
    return dateIsOldEnough(date) && dateIsYoungEnough(date)
}

// Date was at least 13 years ago
fun dateIsOldEnough(date: Date): Boolean {
    val offsetDate = Calendar.getInstance().toDate(Date().time - date.time)
    return offsetDate.year - 1970 >= 13
}

// Date was under 150 years old (mostly believable)
fun dateIsYoungEnough(date: Date): Boolean {
    val offsetDate = Calendar.getInstance().toDate(System.currentTimeMillis() - date.time)
    return offsetDate.year - 1970 <= 150
}

/*
Accepted character code ranges:
a-z = 97-122
A-Z = 65-90
_ = 95
 */

fun isValidUsernameCharacter(char: Char): Boolean {
    return isLetter(char) || char == '_'
}

// Checks whether the character code is between the character codes for 0 and 9 (both inclusive)
fun isDigit(char: Char): Boolean {
    return char.isDigit()
}