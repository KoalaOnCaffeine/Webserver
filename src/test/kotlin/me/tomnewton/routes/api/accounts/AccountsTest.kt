package me.tomnewton.routes.api.accounts

import io.ktor.http.*
import me.tomnewton.routes.test
import me.tomnewton.shared.responses.PAGE_NOT_FOUND
import org.junit.Test

class AccountsTest {

    @Test
    fun testDefaultRoute() {
        test(HttpMethod.Get, "/api/accounts/", PAGE_NOT_FOUND, HttpStatusCode.NotFound)
    }

}