package me.tomnewton.routes.api.teams

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.database.TeamDAO
import me.tomnewton.database.model.Team
import me.tomnewton.database.model.update
import me.tomnewton.plugins.parseObject
import me.tomnewton.shared.responses.teams.TeamCreateFailResponse
import me.tomnewton.shared.responses.teams.TeamCreateSuccessResponse
import java.util.logging.Logger

fun Route.createTeam(accountDAO: AccountDAO, teamDAO: TeamDAO) {
    authenticate("auth-jwt") {
        post("/create") {
            val principal = call.principal<JWTPrincipal>()!!
            val userID = principal.payload.getClaim("user_id").asLong()
            val account = accountDAO.getAccountById(userID)!! // Account cannot be null passed authentication
            val content = parseObject(call.receiveText())

            val name = content["name"]?.toString()
            val description = content["description"]?.toString()
            val imageURL = content["imageURL"]?.toString()

            // If the name isn't provided, you can't create a team
            if (name == null) {
                Logger.getGlobal().info("Team create fail - no team name provided")
                call.respondText(
                    TeamCreateFailResponse().toJsonObject(),
                    ContentType.Application.Json,
                    HttpStatusCode.BadRequest
                )
                return@post
            }

            // Create a team with the given name, description and imageURL, with an empty set of
            val team = Team(
                System.nanoTime(), name, description, mutableListOf(),
                mutableListOf(account.id), /* Include the creator as a manager by default*/
                mutableListOf(), imageURL
            )

            accountDAO.updateAccount(account.id, account.update(teamIDs = account.teamIDs + team.id))
            teamDAO.insertTeam(team)

            Logger.getGlobal().info("Team created successfully - ${team.id}")
            call.respondText(
                TeamCreateSuccessResponse(team.id).toJsonObject(),
                ContentType.Application.Json,
            )

        }
    }
}