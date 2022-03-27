package me.tomnewton.routes.api.accounts

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.tomnewton.plugins.getValue

fun Route.createAccount() {
    post("/create") {
        val username by call
        val email by call
        val password by call
        val dateOfBirth by call
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