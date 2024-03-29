package me.tomnewton.routes.api.accounts

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.date.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.database.AccountDAOImpl
import me.tomnewton.routes.test
import me.tomnewton.shared.responses.accounts.ACCOUNT_CREATE_FAIL
import me.tomnewton.shared.responses.accounts.ACCOUNT_CREATE_SUCCESS
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*

internal const val validUsername = "elizabeth_olsen"
internal const val usernameTooLong = "elizabeth_olsen_wanda" // >20 chars
internal const val usernameInvalidChars = "elizabeth-olsen" // _ is invalid

internal const val validEmail = "elizabeth@olsen.co.po"
internal const val emailNoDomain = "elizabeth@" // No domain
internal const val emailInvalidDomain = "elizabeth@co" // Invalid domain
internal const val emailJustUser = "elizabeth" // No @
internal const val emailJustDomain = "@co.po" // No user

internal const val validPassword = "P0!INTBr3ak!"
internal const val passwordTooShort = "W4nda!" // <7 characters
internal const val passwordNoPunctuation = "P01NTBr3ak" // No punctuation
internal const val passwordNoNumbers = "POINTBreak" // No numbers
internal const val passwordNoCapitals = "p01ntbr3ak" // No capitals
internal const val passwordNoLowercases = "P0INTBR3AK" // No lowercases

internal const val validDateOfBirth = "1989-02-16"

internal val dateOfBirthBorderlineLow = SimpleDateFormat("yyyy-MM-dd").format(
    Calendar.getInstance().toDate(System.currentTimeMillis())
        .minus(Duration.ofDays((365.25 * 13 - 1).toLong()).toMillis()).toJvmDate()
)
internal val dateOfBirthBorderlineHigh = SimpleDateFormat("yyyy-MM-dd").format(
    Calendar.getInstance().toDate(System.currentTimeMillis()).minus(Duration.ofDays((365.25 * 13).toLong()).toMillis())
        .toJvmDate()
)
internal val dateOfBirthTooYoung = SimpleDateFormat("yyyy-MM-dd").format(Date()) // Current date is too young
internal val dateOfBirthWayTooOld = SimpleDateFormat("yyyy-MM-dd").format(
    Date((System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 365.25 * 150).toLong())
)

class CreateTest {

    private fun expect(
        username: String,
        email: String,
        password: String,
        dateOfBirth: String,
        expectedCode: Int,
        expectedStatusCode: HttpStatusCode = HttpStatusCode.OK,
        accountDAO: AccountDAO = AccountDAOImpl()
    ) {
        test(
            HttpMethod.Post,
            "/api/accounts/create",
            expectedCode,
            expectedStatusCode,
            accountDAO = accountDAO,
            builder = {
                setBody("""{"username": "$username", "email": "$email", "password": "$password", "dateOfBirth" : "$dateOfBirth"}""")
            })
    }

    @Test
    fun testAllValid() {
        expect(validUsername, validEmail, validPassword, validDateOfBirth, ACCOUNT_CREATE_SUCCESS, HttpStatusCode.OK)
    }

    @Test
    fun testUsernameTooLong() {
        expect(
            usernameTooLong, validEmail, validPassword, validDateOfBirth, ACCOUNT_CREATE_FAIL, HttpStatusCode.BadRequest
        )
    }

    @Test
    fun testUsernameInvalidCharacters() {
        expect(
            usernameInvalidChars,
            validEmail,
            validPassword,
            validDateOfBirth,
            ACCOUNT_CREATE_FAIL,
            HttpStatusCode.BadRequest
        )
    }

    @Test
    fun testEmailNoDomain() {
        expect(
            validUsername,
            emailNoDomain,
            validPassword,
            validDateOfBirth,
            ACCOUNT_CREATE_FAIL,
            HttpStatusCode.BadRequest
        )
    }

    @Test
    fun testEmailInvalidDomain() {
        expect(
            validUsername,
            emailInvalidDomain,
            validPassword,
            validDateOfBirth,
            ACCOUNT_CREATE_FAIL,
            HttpStatusCode.BadRequest
        )
    }

    @Test
    fun testEmailJustUser() {
        expect(
            validUsername,
            emailJustUser,
            validPassword,
            validDateOfBirth,
            ACCOUNT_CREATE_FAIL,
            HttpStatusCode.BadRequest
        )
    }

    @Test
    fun testEmailJustDomain() {
        expect(
            validUsername,
            emailJustDomain,
            validPassword,
            validDateOfBirth,
            ACCOUNT_CREATE_FAIL,
            HttpStatusCode.BadRequest
        )
    }

    @Test
    fun testPasswordTooShort() {
        expect(
            validUsername,
            validEmail,
            passwordTooShort,
            validDateOfBirth,
            ACCOUNT_CREATE_FAIL,
            HttpStatusCode.BadRequest
        )
    }

    @Test
    fun testPasswordNoPunctuation() {
        expect(
            validUsername,
            validEmail,
            passwordNoPunctuation,
            validDateOfBirth,
            ACCOUNT_CREATE_FAIL,
            HttpStatusCode.BadRequest
        )
    }

    @Test
    fun testPasswordNoNumbers() {
        expect(
            validUsername,
            validEmail,
            passwordNoNumbers,
            validDateOfBirth,
            ACCOUNT_CREATE_FAIL,
            HttpStatusCode.BadRequest
        )
    }

    @Test
    fun testPasswordNoCapitals() {
        expect(
            validUsername,
            validEmail,
            passwordNoCapitals,
            validDateOfBirth,
            ACCOUNT_CREATE_FAIL,
            HttpStatusCode.BadRequest
        )
    }

    @Test
    fun testPasswordNoLowercases() {
        expect(
            validUsername,
            validEmail,
            passwordNoLowercases,
            validDateOfBirth,
            ACCOUNT_CREATE_FAIL,
            HttpStatusCode.BadRequest
        )
    }

    @Test
    fun testDateOfBirthBorderlineLow() {
        expect(
            validUsername,
            validEmail,
            validPassword,
            dateOfBirthBorderlineLow,
            ACCOUNT_CREATE_FAIL,
            HttpStatusCode.BadRequest
        )
    }

    @Test
    fun testDateOfBirthBorderlineHigh() {
        expect(
            validUsername,
            validEmail,
            validPassword,
            dateOfBirthBorderlineHigh,
            ACCOUNT_CREATE_SUCCESS,
            HttpStatusCode.OK
        )
    }

    @Test
    fun testDateOfBirthTooYoung() {
        expect(
            validUsername,
            validEmail,
            validPassword,
            dateOfBirthTooYoung,
            ACCOUNT_CREATE_FAIL,
            HttpStatusCode.BadRequest
        )
    }

    @Test
    fun testDateOfBirthWayTooOld() {
        expect(
            validUsername,
            validEmail,
            validPassword,
            dateOfBirthWayTooOld,
            ACCOUNT_CREATE_FAIL,
            HttpStatusCode.BadRequest
        )
    }

    @Test
    fun testAccountUsernameAlreadyExists() {
        expect(
            validUsername,
            "valid@email.com", // Avoid testing the email too
            validPassword,
            validDateOfBirth,
            ACCOUNT_CREATE_FAIL,
            HttpStatusCode.Conflict,
            AccountDAOImpl(
                mutableMapOf(
                    elizabethOlsenAccount.id to elizabethOlsenAccount
                )
            )
        )
    }

    @Test
    fun testAccountEmailAlreadyExists() {
        expect(
            "validusername", // Avoid testing the username too
            validEmail,
            validPassword,
            validDateOfBirth,
            ACCOUNT_CREATE_FAIL,
            HttpStatusCode.Conflict,
            AccountDAOImpl(
                mutableMapOf(
                    0L to elizabethOlsenAccount
                )
            )
        )
    }

}