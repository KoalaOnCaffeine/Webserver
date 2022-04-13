package me.tomnewton.routes.api.accounts

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import me.tomnewton.plugins.parseObject
import me.tomnewton.routes.test
import me.tomnewton.shared.responses.accounts.ACCOUNT_CREATE_FAIL
import me.tomnewton.shared.responses.accounts.ACCOUNT_CREATE_SUCCESS
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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

internal const val validDateOfBirth = "16/02/1989"
internal val dateOfBirthTooYoung = SimpleDateFormat("dd/MM/yyyy").format(Date()) // Current date is too young

class CreateTest {

    private fun postWith(
        username: String,
        email: String,
        password: String,
        dateOfBirth: String,
        builder: HttpRequestBuilder.() -> Unit = {},
        response: suspend HttpResponse.() -> Unit
    ) {
        test(HttpMethod.Post, "/api/accounts/create", {
            builder(this)
            setBody("""{"username": "$username", "email": "$email", "password": "$password", "dateOfBirth" : "$dateOfBirth"}""")
        }) { runBlocking { response(this@test) } }
    }

    private fun test(
        username: String,
        email: String,
        password: String,
        dateOfBirth: String,
        expectedStatus: HttpStatusCode,
        expectedCode: Int
    ): Boolean {
        postWith(username, email, password, dateOfBirth) {
            assertContains(contentType()?.contentType ?: "", ContentType.Application.Json.contentType)
            assertEquals(expectedStatus, status)
            val json = parseObject(bodyAsText())
            assertEquals(
                expectedCode, json.getOrDefault("code", -1).toString().toIntOrNull()
            ) // Defaults to long which isn't right
        }
        return true // No error, so it must have worked
    }

    @Test
    fun testAllValid() = assertTrue {
        test(validUsername, validEmail, validPassword, validDateOfBirth, HttpStatusCode.OK, ACCOUNT_CREATE_SUCCESS)
    }

    @Test
    fun testUsernameTooLong() = assertTrue {
        test(
            usernameTooLong, validEmail, validPassword, validDateOfBirth, HttpStatusCode.BadRequest, ACCOUNT_CREATE_FAIL
        )
    }

    @Test
    fun testUsernameInvalidCharacters() = assertTrue {
        test(
            usernameInvalidChars,
            validEmail,
            validPassword,
            validDateOfBirth,
            HttpStatusCode.BadRequest,
            ACCOUNT_CREATE_FAIL
        )
    }

    @Test
    fun testEmailNoDomain() = assertTrue {
        test(
            validUsername,
            emailNoDomain,
            validPassword,
            validDateOfBirth,
            HttpStatusCode.BadRequest,
            ACCOUNT_CREATE_FAIL
        )
    }

    @Test
    fun testEmailInvalidDomain() = assertTrue {
        test(
            validUsername,
            emailInvalidDomain,
            validPassword,
            validDateOfBirth,
            HttpStatusCode.BadRequest,
            ACCOUNT_CREATE_FAIL
        )
    }

    @Test
    fun testEmailJustUser() = assertTrue {
        test(
            validUsername,
            emailJustUser,
            validPassword,
            validDateOfBirth,
            HttpStatusCode.BadRequest,
            ACCOUNT_CREATE_FAIL
        )
    }

    @Test
    fun testEmailJustDomain() = assertTrue {
        test(validUsername, emailJustDomain, validPassword, validDateOfBirth, HttpStatusCode.BadRequest, DEFAULT_PORT)
    }

    @Test
    fun testPasswordTooShort() = assertTrue {
        test(
            validUsername,
            validEmail,
            passwordTooShort,
            validDateOfBirth,
            HttpStatusCode.BadRequest,
            ACCOUNT_CREATE_FAIL
        )
    }

    @Test
    fun testPasswordNoPunctuation() = assertTrue {
        test(
            validUsername,
            validEmail,
            passwordNoPunctuation,
            validDateOfBirth,
            HttpStatusCode.BadRequest,
            ACCOUNT_CREATE_FAIL
        )
    }

    @Test
    fun testPasswordNoNumbers() = assertTrue {
        test(
            validUsername,
            validEmail,
            passwordNoNumbers,
            validDateOfBirth,
            HttpStatusCode.BadRequest,
            ACCOUNT_CREATE_FAIL
        )
    }

    @Test
    fun testPasswordNoCapitals() = assertTrue {
        test(
            validUsername,
            validEmail,
            passwordNoCapitals,
            validDateOfBirth,
            HttpStatusCode.BadRequest,
            ACCOUNT_CREATE_FAIL
        )
    }

    @Test
    fun testPasswordNoLowercases() = assertTrue {
        test(
            validUsername,
            validEmail,
            passwordNoLowercases,
            validDateOfBirth,
            HttpStatusCode.BadRequest,
            ACCOUNT_CREATE_FAIL
        )
    }

    @Test
    fun testDateOfBirthTooYoung() = assertTrue {
        test(
            validUsername,
            validEmail,
            validPassword,
            dateOfBirthTooYoung,
            HttpStatusCode.BadRequest,
            ACCOUNT_CREATE_FAIL
        )
    }

}