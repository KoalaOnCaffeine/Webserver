package me.tomnewton.routes.api.accounts

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.plugins.parameter

fun Route.getAccount(accountDAO: AccountDAO) {
    get("/{id}") {
        val accountID = call.parameter("id", String::toIntOrNull)
        if (accountID == null) {
            call.respondText("Invalid account ID")
            return@get
        }
        val account = accountDAO.getAccountById(accountID)
        if (account == null) {
            call.respondText("No account found with that ID")
            return@get
        }
        call.respondText(account.toJsonObject(), ContentType.Application.Json)
    }
}