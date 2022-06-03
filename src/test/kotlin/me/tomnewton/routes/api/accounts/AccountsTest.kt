package me.tomnewton.routes.api.accounts

import io.ktor.client.statement.*
import io.ktor.http.*
import me.tomnewton.plugins.parseObject
import me.tomnewton.routes.test
import me.tomnewton.shared.responses.PAGE_NOT_FOUND
import org.junit.Test
import kotlin.test.assertEquals

class AccountsTest {

    @Test
    fun testDefaultRoute() {
        test(HttpMethod.Get, "/api/accounts/", expectedStatusCode = HttpStatusCode.NotFound) {
            val body = bodyAsText()
            val json = parseObject(body)
            assertEquals(PAGE_NOT_FOUND, json["code"]?.toString()?.toIntOrNull() ?: Int.MIN_VALUE)
        }
    }

}