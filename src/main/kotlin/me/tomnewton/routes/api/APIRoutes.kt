package me.tomnewton.routes.api

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.tomnewton.plugins.getValue
import me.tomnewton.plugins.parameter

fun Route.apiRoutes() {
    route("/api/{version}") {
        // Respond to /api/{version}
        get("") {
            val version = call.parameters["version"]?.toIntOrNull() ?: 0
            call.respondText("Requested version $version")
        }
        route("/accounts", Route::accountRoutes)
    }
}

private fun Route.accountRoutes() {

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

    get("/{id}") {
        val accountID = call.parameter("id", String::toIntOrNull)
        call.respondText(accountID?.let { "Requested the account with the id '${accountID}'" } ?: "Invalid account ID")
    }

}