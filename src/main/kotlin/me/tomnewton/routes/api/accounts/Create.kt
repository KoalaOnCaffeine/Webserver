package me.tomnewton.routes.api.accounts

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.plugins.parseObject
import me.tomnewton.database.model.Account
import me.tomnewton.shared.responses.accounts.AccountCreateFailResponse
import me.tomnewton.shared.responses.accounts.AccountCreateSuccessResponse

fun Route.createAccount(accountDAO: AccountDAO) {
    post("/create") {
        val content = parseObject(call.receiveText())
        val username: String by content
        val email: String by content
        val password: String by content
        val dateOfBirth: String by content

        val account = Account(
            System.nanoTime(), // Should be unique enough?
            username,
            email,
            password,
            dateOfBirth,
            listOf(), // Should be empty by default
            listOf(), // Should be empty by default
            "https://i.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U" // Default image url
        )

        val success = accountDAO.insertAccount(account)

        if (success) {
            // Send the account object, safe since they just made it, so they know all the information
            val response = AccountCreateSuccessResponse(account.id)
            call.respondText(response.toJsonObject())
        } else {
            // Send appropriate error, such as the database failed
            val response = AccountCreateFailResponse()
            call.respondText(response.toJsonObject())
        }

    }
}