package me.tomnewton.routes.api.accounts

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.plugins.parseObject
import me.tomnewton.shared.Account

fun Route.createAccount(accountDAO: AccountDAO) {
    post("/create") {
        val content = parseObject(call.receiveText())

        val username: String by content
        val email: String by content
        val password: String by content
        val dateOfBirth: String by content

        val account = Account(username, email, password, dateOfBirth)
        val id = accountDAO.countAccounts() // If there are 0 accounts, ID=0, 1 accounts, ID=1...

        val success = accountDAO.insertAccount(id, account)

        if (success) {
            // Send the account object, safe since they just made it, so they know all the information
            call.respondText(account.toJsonObject())
        } else {
            // Send appropriate error
            call.respondText("An error occurred")
        }

    }
}