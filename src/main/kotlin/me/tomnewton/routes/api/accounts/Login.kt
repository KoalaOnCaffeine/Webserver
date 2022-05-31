package me.tomnewton.routes.api.accounts

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.plugins.parseObject
import me.tomnewton.routes.api.createTokenFor
import me.tomnewton.shared.responses.accounts.AccountLoginFailResponse
import me.tomnewton.shared.responses.accounts.AccountLoginSuccessResponse
import java.util.logging.Logger

// Login route is used to provide authentication for the dashboard for someone who has an account, but isn't logged in
fun Route.login(accountDAO: AccountDAO) {
    post("/login") {
        val content = parseObject(call.receiveText())
        val username = content.getOrDefault("username", null) as String?
        val password = content.getOrDefault("password", null) as String?
        if (setOf(username, password).size != 2) {
            Logger.getGlobal().info("Account login fail - not all information provided")
            call.respondText(
                AccountLoginFailResponse("You must provide a username and password to log in").toJsonObject(),
                ContentType.Application.Json,
                HttpStatusCode.BadRequest
            )
            return@post
        }
        val account = accountDAO.getAccountByUsername(username!!)
        if (account == null) {
            Logger.getGlobal().info("Account login fail - no account found")
            call.respondText(
                AccountLoginFailResponse("No account found with that username").toJsonObject(),
                ContentType.Application.Json,
                HttpStatusCode.BadRequest
            )
            return@post
        }
        if (account.password != password) {
            Logger.getGlobal().info("Account login fail - invalid username or password")
            call.respondText(
                AccountLoginFailResponse("Invalid username or password").toJsonObject(),
                ContentType.Application.Json,
                HttpStatusCode.BadRequest
            )
            return@post
        }
        val token = createTokenFor(account.id)
        // Respond with the token claim and let the client take them to the dashboard
        Logger.getGlobal().info("Account login successful, $token")
        call.respondText(AccountLoginSuccessResponse(token).toJsonObject(), ContentType.Application.Json)
    }
}