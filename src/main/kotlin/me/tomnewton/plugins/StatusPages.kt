package me.tomnewton.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import me.tomnewton.Messages

fun Application.configureStatusPages() {
    install(StatusPages) {

        // For any page which returned the not found code
        status(HttpStatusCode.NotFound) { call, _ ->
            // Respond with the notFoundMessage in a json format
            call.respondText(Messages.notFoundMessage, ContentType.Application.Json, HttpStatusCode.NotFound)
        }
    }
}