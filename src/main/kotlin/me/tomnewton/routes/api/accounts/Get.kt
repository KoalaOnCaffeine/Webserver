package me.tomnewton.routes.api.accounts

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.plugins.parameter
import me.tomnewton.shared.responses.accounts.AccountGetFailResponse
import me.tomnewton.shared.responses.accounts.AccountGetSuccessResponse

fun Route.getAccount(accountDAO: AccountDAO) {
    get("/{id}") {
        val accountID = call.parameter("id", String::toIntOrNull)
        if (accountID == null) {
            call.respondText("Invalid account ID")
            return@get
        }
        val account = accountDAO.getAccountById(accountID)
        if (account == null) {
            val response = AccountGetFailResponse()
            call.respondText(response.toJsonObject())
            return@get
        }

        // Replace with response message
        val response = AccountGetSuccessResponse(account)
        call.respondText(response.toJsonObject(), ContentType.Application.Json)
    }
}