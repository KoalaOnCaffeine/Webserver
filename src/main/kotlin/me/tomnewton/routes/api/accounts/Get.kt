package me.tomnewton.routes.api.accounts

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.tomnewton.plugins.parameter

fun Route.getAccount() {
    get("/{id}") {
        val accountID = call.parameter("id", String::toIntOrNull)
        call.respondText(accountID?.let { "Requested the account with the id '${accountID}'" } ?: "Invalid account ID")
    }
}