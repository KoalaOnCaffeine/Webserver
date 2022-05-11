package me.tomnewton.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import me.tomnewton.shared.responses.PageNotFoundResponse
import java.util.logging.Logger

fun Application.configureStatusPages() {
    install(StatusPages) {

        // For any page which returned the not found code
        status(HttpStatusCode.NotFound) { call, _ ->
            // Respond with the notFoundMessage in a json format
            Logger.getGlobal().info("Page not found")
            val response = PageNotFoundResponse()
            call.respondText(response.toJsonObject(), ContentType.Application.Json, HttpStatusCode.NotFound)
        }
    }
}