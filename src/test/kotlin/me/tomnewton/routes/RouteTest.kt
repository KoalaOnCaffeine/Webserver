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

internal fun test(
    method: HttpMethod, route: String, builder: HttpRequestBuilder.() -> Unit, test: HttpResponse.() -> Unit
) {
    val accountDAO = AccountDAOImpl()
    testApplication {
        application {
            configureSecurity()
            configureRouting(accountDAO)
            configureHTTP()
            configureStatusPages()
        }
        val response = client.request {
            builder(this)
            this.method = method
            url(route)
        }

        test(response)

    }
}