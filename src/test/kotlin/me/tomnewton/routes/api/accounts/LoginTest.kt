package me.tomnewton.routes.api.accounts

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.database.AccountDAOImpl
import me.tomnewton.plugins.parseObject
import me.tomnewton.routes.test
import me.tomnewton.shared.responses.accounts.ACCOUNT_LOGIN_FAIL
import me.tomnewton.shared.responses.accounts.ACCOUNT_LOGIN_SUCCESS
import org.junit.Test
import kotlin.test.assertEquals

class LoginTest {


    private fun expect(
        username: String,
        password: String,
        expectedCode: Int,
        expectedStatusCode: HttpStatusCode = HttpStatusCode.OK,
        expectedContentType: ContentType = ContentType.Application.Json,
        accountDAO: AccountDAO = AccountDAOImpl(),
    ) {
        test(
            HttpMethod.Post,
            "/api/accounts/login",
            accountDAO,
            expectedContentType = expectedContentType,
            expectedStatusCode = expectedStatusCode,
            builder = {
                setBody("""{"username": "$username", "password": "$password" }""")
            }
        ) {
            val body = bodyAsText()
            val json = parseObject(body)
            assertEquals(expectedCode, json["code"]?.toString()?.toIntOrNull() ?: Int.MIN_VALUE)
        }
    }

    @Test
    fun testLoginValidUsernameAndPassword() {
        expect(
            elizabethOlsenAccount.username,
            elizabethOlsenAccount.password,
            ACCOUNT_LOGIN_SUCCESS,
            HttpStatusCode.OK,
            accountDAO = filledAccountDAO
        )
    }

    @Test
    fun testLoginInvalidUsername() {
        // Username doesn't exist, but the password matches some account
        expect(
            elizabethOlsenAccount.username + "chrishemsworth",
            elizabethOlsenAccount.password,
            ACCOUNT_LOGIN_FAIL,
            HttpStatusCode.BadRequest,
            accountDAO = filledAccountDAO
        )
    }

    @Test
    fun testLoginInvalidPassword() {
        // Username does exist, but the password is wrong
        expect(
            elizabethOlsenAccount.username,
            elizabethOlsenAccount.password + "chrishemsworth",
            ACCOUNT_LOGIN_FAIL,
            HttpStatusCode.BadRequest,
            accountDAO = filledAccountDAO
        )
    }

}