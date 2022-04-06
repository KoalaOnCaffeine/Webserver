package me.tomnewton.routes.api.accounts

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.database.model.Account
import me.tomnewton.plugins.parseObject
import me.tomnewton.shared.responses.EMPTY_RESPONSE
import me.tomnewton.shared.responses.Response
import me.tomnewton.shared.responses.accounts.AccountCreateFailResponse
import me.tomnewton.shared.responses.accounts.AccountCreateSuccessResponse
import java.lang.Character.isLetter

internal const val defaultImage =
    "https://i.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U"

fun Route.createAccount(accountDAO: AccountDAO) {
    post("/create") {
        val content = parseObject(call.receiveText())
        val username: String by content
        val email: String by content
        val password: String by content
        val dateOfBirth: String by content

        // Projects and teams should be empty after creating an account
        val account =
            Account(System.nanoTime(), username, email, password, dateOfBirth, listOf(), listOf(), defaultImage)

        val (valid, validateResponse) = validate(account)

        if(valid) {
            val insertResponse = insert(account, accountDAO)
            call.respondText(insertResponse.toJsonObject())
        } else {
            call.respondText(validateResponse.toJsonObject())
        }
    }
}

private fun validate(account: Account): Pair<Boolean, Response> {
    if (!isValidUsername(account.username)) return false to AccountCreateFailResponse("Username contain only letters, digits and underscores, and be a maximum of 20 characters long")
    if (!isValidEmail(account.email)) return false to AccountCreateFailResponse("Invalid email")
    if (!isValidPassword(account.password)) return false to AccountCreateFailResponse("Password must contain a capital, punctuation, number and be at least 7 characters long")

    // TODO Check date of birth and create that function

    return true to EMPTY_RESPONSE
}

private fun insert(account: Account, accountDAO: AccountDAO): Response {
    val success = accountDAO.insertAccount(account)
    return if (success) AccountCreateSuccessResponse(account.id)
    else AccountCreateFailResponse("Unable to create the account. This is a server error - please try again")
}

// USERNAME

fun isValidUsername(username: String): Boolean {
    return usernameIsValidLength(username) && usernameOnlyValidChars(username)
}

fun usernameIsValidLength(username: String): Boolean {
    return username.length in 1..20
}

fun usernameOnlyValidChars(username: String): Boolean {
    return !username.any { !isValidUsernameCharacter(it) }
}

// EMAIL

// https://www.w3resource.com/javascript/form/email-validation.php

fun isValidEmail(email: String): Boolean {
    return emailContainsValidUsername(email) && emailContainsValidDomain(email)
}

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
    return containsLowerCase(password)
            && containsCapital(password)
            && containsPunctuation(password)
            && containsDigit(password)
            && passwordIsValidLength(password)
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
    return true
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