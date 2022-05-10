package me.tomnewton.routes.api.accounts

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import me.tomnewton.database.AccountDAOImpl
import me.tomnewton.database.model.Account
import me.tomnewton.plugins.parseObject
import me.tomnewton.routes.test
import me.tomnewton.shared.responses.accounts.ACCOUNT_GET_SUCCESS
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

internal val elizabethOlsenAccount = Account(
    0, validUsername, validPassword, validEmail, validDateOfBirth, mutableListOf(), mutableListOf(), defaultImage
)

internal class GetKtTest {

    private fun get(id: Long, builder: HttpRequestBuilder.() -> Unit = {}, response: suspend HttpResponse.() -> Unit) {
        // A new one every get, just to make sure that it always contains the account
        val filledAccountDAO = AccountDAOImpl(mutableMapOf(elizabethOlsenAccount.id to elizabethOlsenAccount))
        test(HttpMethod.Get, "/api/accounts/$id/", {
            builder(this)
        }, filledAccountDAO) {
            runBlocking { response(this@test) }
        }
    }

    private fun test(id: Long, expectedStatus: HttpStatusCode, expectedBody: String, expectedCode: Int) {
        get(id) {
            assertContains(contentType()?.contentType ?: "", ContentType.Application.Json.contentType)
            assertEquals(expectedStatus, status)
            val body = bodyAsText()
            val json = parseObject(body)
            assertEquals(expectedBody, body)
            assertEquals(expectedCode, json.getOrDefault("code", -1).toString().toIntOrNull())
        }
    }

    @Test
    fun testGetPresentAccount() {
        test(elizabethOlsenAccount.id, HttpStatusCode.OK, elizabethOlsenAccount.toJsonObject(), ACCOUNT_GET_SUCCESS)
    }

}