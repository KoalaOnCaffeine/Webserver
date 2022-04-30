package me.tomnewton.routes.api.accounts

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.plugins.parseObject
import me.tomnewton.routes.api.createTokenFor
import me.tomnewton.shared.responses.accounts.AccountLoginFailResponse
import me.tomnewton.shared.responses.accounts.AccountLoginSuccessResponse

// Login route is used to provide authentication for the dashboard for someone who has an account, but isn't logged in
fun Route.login(accountDAO: AccountDAO) {
    authenticate("auth-jwt") {
        // test for tokens
        get("/test") {
            val principal = call.principal<JWTPrincipal>()!!
            println(principal.payload.claims)
            val id = principal.payload.getClaim("user_id").asLong()
            val username = accountDAO.getAccountById(id)?.username ?: "null"
            call.respondText("Hello, $username")
        }
    }
    post("/login") {
        val content = parseObject(call.receiveText())
        val username = content.getOrDefault("username", null) as String?
        val password = content.getOrDefault("password", null) as String?
        if (setOf(username, password).size != 2) {
            call.respondText(
                AccountLoginFailResponse("You must provide a username and password to log in").toJsonObject(),
                ContentType.Application.Json,
                HttpStatusCode.BadRequest
            )
            return@post
        }
        val account = accountDAO.getAccountByUsername(username!!)
        if (account == null) {
            call.respondText(
                AccountLoginFailResponse("No account found with that username").toJsonObject(),
                ContentType.Application.Json,
                HttpStatusCode.BadRequest
            )
            return@post
        }
        if (account.password != password) {
            call.respondText(
                AccountLoginFailResponse("Invalid username or password").toJsonObject(),
                ContentType.Application.Json,
                HttpStatusCode.BadRequest
            )
            return@post
        }
        val token = createTokenFor(account.id)
        // Respond with the token claim and let the client take them to the dashboard
        call.respondText(AccountLoginSuccessResponse(token).toJsonObject(), ContentType.Application.Json)
    }
}