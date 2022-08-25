package me.tomnewton.routes.api.accounts

import io.ktor.util.date.*
import me.tomnewton.database.model.Account
import me.tomnewton.shared.responses.EMPTY_RESPONSE
import me.tomnewton.shared.responses.Response
import me.tomnewton.shared.responses.accounts.AccountCreateFailResponse
import java.text.SimpleDateFormat
import java.util.*

internal fun validate(account: Account): Pair<Boolean, Response> {
    if (!isValidUsername(account.username)) return false to AccountCreateFailResponse("Username contain only letters, digits and underscores, and be a maximum of 20 characters long")
    if (!isValidEmail(account.email)) return false to AccountCreateFailResponse("Invalid email")
    if (!isValidPassword(account.password)) return false to AccountCreateFailResponse("Password must contain a capital, punctuation, number and be at least 7 characters long")
    if (!isValidDateOfBirth(account.dateOfBirth)) return false to AccountCreateFailResponse("Invalid date of birth. You must be at least 13 years old to create an account")

    return true to EMPTY_RESPONSE
}

// USERNAME

internal fun isValidUsername(username: String): Boolean {
    return usernameIsValidLength(username) && usernameOnlyValidChars(username)
}

// Username in the range of 1 to 20 (both inclusive)
internal fun usernameIsValidLength(username: String): Boolean {
    return username.length in 1..20
}

// Username doesn't contain any characters that aren't valid
internal fun usernameOnlyValidChars(username: String): Boolean {
    return !username.any { !isValidUsernameCharacter(it) }
}

// EMAIL

// https://www.w3resource.com/javascript/form/email-validation.php
internal fun isValidEmail(email: String): Boolean {
    return emailContainsValidUsername(email) && emailContainsValidDomain(email)
}

// Checks that the email contains a valid username before the @
internal fun emailContainsValidUsername(email: String): Boolean {
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
internal fun emailContainsValidDomain(email: String): Boolean {
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
internal fun isValidPassword(password: String): Boolean {
    return containsLowerCase(password) && containsCapital(password) && containsPunctuation(password) && containsDigit(
        password
    ) && passwordIsValidLength(password)
}

// Password must contain a lowercase letter, which is any letter whose lowercase is itself
internal fun containsLowerCase(password: String): Boolean {
    return password.any { it.isLowerCase() }
}

// Password must contain a capital letter, which is any letter whose uppercase is itself
internal fun containsCapital(password: String): Boolean {
    return password.any { it.isUpperCase() }
}

// Password must contain punctuation, which is anything satisfying !isLetter and !isDigit
internal fun containsPunctuation(password: String): Boolean {
    return password.any { !(isDigit(it) || Character.isLetter(it)) }
}

// Password must contain a digit, which is anything satisfying the isDigit fun
internal fun containsDigit(password: String): Boolean {
    return password.any(::isDigit)
}

// Password must be at least 7 characters long
internal fun passwordIsValidLength(password: String): Boolean {
    return password.length >= 7
}

// DATE OF BIRTH

internal fun isValidDateOfBirth(dateOfBirth: String): Boolean {
    val date = SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirth.replace('/', '-'))
    return dateIsOldEnough(date) && dateIsYoungEnough(date)
}

// Date was at least 13 years ago
internal fun dateIsOldEnough(date: Date): Boolean {
    val offsetDate = Calendar.getInstance().toDate(Date().time - date.time)
    return offsetDate.year - 1970 >= 13
}

// Date was under 150 years old (mostly believable)
internal fun dateIsYoungEnough(date: Date): Boolean {
    val offsetDate = Calendar.getInstance().toDate(System.currentTimeMillis() - date.time)
    return offsetDate.year - 1970 <= 150
}

/*
Accepted character code ranges:
a-z = 97-122
A-Z = 65-90
_ = 95
 */

internal fun isValidUsernameCharacter(char: Char): Boolean {
    return Character.isLetter(char) || char == '_'
}

// Checks whether the character code is between the character codes for 0 and 9 (both inclusive)
internal fun isDigit(char: Char): Boolean {
    return char.isDigit()
}