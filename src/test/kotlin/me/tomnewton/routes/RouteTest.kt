package me.tomnewton.routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import me.tomnewton.database.AccountDAOImpl
import me.tomnewton.plugins.configureHTTP
import me.tomnewton.plugins.configureRouting
import me.tomnewton.plugins.configureSecurity
import me.tomnewton.plugins.configureStatusPages

// Conduct a test on a route, with a given method
internal fun test(
    method: HttpMethod, route: String, builder: HttpRequestBuilder.() -> Unit, test: HttpResponse.() -> Unit
) {
    val accountDAO = AccountDAOImpl()
    testApplication {
        application {
            // Install all the plugins
            configureSecurity()
            configureRouting(accountDAO)
            configureHTTP()
            configureStatusPages()
        }
        val response = client.request {
            // Let the builder edit the request, then set the method and route
            builder(this)
            this.method = method
            url(route)
        }

        // Run the test method on the response
        test(response)

    }
}