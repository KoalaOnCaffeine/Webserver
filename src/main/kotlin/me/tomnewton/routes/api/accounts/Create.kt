package me.tomnewton.routes.api.accounts

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.database.model.Account
import me.tomnewton.plugins.parseObject
import me.tomnewton.routes.api.createTokenFor
import me.tomnewton.shared.responses.Response
import me.tomnewton.shared.responses.accounts.AccountCreateFailResponse
import me.tomnewton.shared.responses.accounts.AccountCreateSuccessResponse
import java.util.logging.Logger

internal const val defaultImage =
    "https://i.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U"

fun Route.createAccount(accountDAO: AccountDAO) {
    post("/create") {
        val content = parseObject(call.receiveText())
        val username = content.getOrDefault("username", null) as String?
        val email = content.getOrDefault("email", null) as String?
        val password = content.getOrDefault("password", null) as String?
        val dateOfBirth = content.getOrDefault("dateOfBirth", null) as String?

        if (setOf(username, email, password, dateOfBirth).size != 4) {
            lackOfDetails()
            return@post
        }

        // Projects and teams should be empty after creating an account
        val account =
            Account(System.nanoTime(), username!!, email!!, password!!, dateOfBirth!!, listOf(), listOf(), defaultImage)

        val (valid, validateResponse) = validate(account)

        if (!valid) {
            invalidDetails(validateResponse)
            return@post
        }

        val success = accountDAO.insertAccount(account)

        if (!success) {
            accountCreateFail()
            return@post
        }

        // Create a JWT for the client to handle, with a shared secret
        val token = createTokenFor(account.id)
        val insertResponse = AccountCreateSuccessResponse(account.id, token)
        accountCreateSuccess(account, insertResponse)

    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.lackOfDetails() {
    // One or more of them were null, tell them
    Logger.getGlobal().info("Account create fail - not all information provided")
    call.respondText(
        AccountCreateFailResponse("Must provide a username, email, password and dateOfBirth field").toJsonObject(),
        ContentType.Application.Json,
        HttpStatusCode.BadRequest
    )
}

private suspend fun PipelineContext<Unit, ApplicationCall>.invalidDetails(
    validateResponse: Response
) {
    // Details were invalid
    Logger.getGlobal().info("Account create fail - invalid account details")
    call.respondText(validateResponse.toJsonObject(), ContentType.Application.Json, HttpStatusCode.BadRequest)
}

private suspend fun PipelineContext<Unit, ApplicationCall>.accountCreateFail() {
    // Account already existed, so don't create a new one
    Logger.getGlobal().info("Account create fail - not all information provided")
    call.respondText(
        AccountCreateFailResponse("An account with this username or email already exists").toJsonObject(),
        ContentType.Application.Json,
        HttpStatusCode.Conflict
    )
}

private suspend fun PipelineContext<Unit, ApplicationCall>.accountCreateSuccess(
    account: Account,
    insertResponse: AccountCreateSuccessResponse
) {
    Logger.getGlobal().info("Account created successfully - ${account.id}")
    call.respondText(insertResponse.toJsonObject(), ContentType.Application.Json)
}
