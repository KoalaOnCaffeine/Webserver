package me.tomnewton.routes.api.accounts

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.plugins.parseObject

fun Route.createAccount(accountDAO: AccountDAO) {
    post("/create") {
        val content = parseObject(call.receiveText())

        val username: String by content
        val email: String by content
        val password: String by content
        val dateOfBirth: String by content
        call.respondText(
            """
            username: $username
            email: $email
            password: $password
            dateOfBirth: $dateOfBirth
        """.trimIndent()
        )
    }
}