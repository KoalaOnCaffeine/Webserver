package me.tomnewton.routes

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.database.AccountDAOImpl
import me.tomnewton.database.TeamDAO
import me.tomnewton.database.TeamDAOImpl
import me.tomnewton.plugins.configureHTTP
import me.tomnewton.plugins.configureRouting
import me.tomnewton.plugins.configureSecurity
import me.tomnewton.plugins.configureStatusPages
import kotlin.test.assertEquals

// Conduct a test on a route, with a given method
internal fun test(
    method: HttpMethod,
    route: String,
    accountDAO: AccountDAO = AccountDAOImpl(),
    teamDAO: TeamDAO = TeamDAOImpl(),
    expectedContentType: ContentType = ContentType.Application.Json,
    expectedStatusCode: HttpStatusCode = HttpStatusCode.OK,
    builder: HttpRequestBuilder.() -> Unit = {},
    test: suspend HttpResponse.() -> Unit
) {

    testApplication {
        application {
            // Install all the plugins
            configureSecurity()
            configureRouting(accountDAO, teamDAO)
            configureHTTP()
            configureStatusPages()
        }

        val response = client.request {
            expectSuccess = false
            // Let the builder edit the request, then set the method and route
            builder(this)
            this.method = method
            url(route)
        }

        assertEquals(expectedContentType, response.contentType()?.withoutParameters())
        assertEquals(expectedStatusCode, response.status)

        // Run the test method on the response
        test(response)
    }
}