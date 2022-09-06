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
import me.tomnewton.database.model.update
import me.tomnewton.plugins.parseObject
import me.tomnewton.shared.responses.teams.TeamUpdateFailResponse
import me.tomnewton.shared.responses.teams.TeamUpdateSuccessResponse
import java.util.logging.Logger

fun Route.updateTeam(accountDAO: AccountDAO, teamDAO: TeamDAO) {
    authenticate("auth-jwt") {
        post("/update") {
            val principal = call.principal<JWTPrincipal>()!!
            val userID = principal.payload.getClaim("user_id").asLong()
            val account = accountDAO.getAccountById(userID)!! // Account cannot be null passed authentication
            val content = parseObject(call.receiveText())

            val id = content["id"]?.toString()?.toLongOrNull()
            val name = content["name"]?.toString()
            val description = content["description"]?.toString()
            val imageURL = content["imageURL"]?.toString()

            // No id provided
            if (id == null) {
                Logger.getGlobal().info("No ID provided for update")
                call.respondText(
                    TeamUpdateFailResponse("No id field provided").toJsonObject(),
                    ContentType.Application.Json,
                    HttpStatusCode.BadRequest
                )
                return@post
            }

            val team = teamDAO.getTeamById(id)

            // Team wasn't found, or they don't have access to edit it
            if (team == null || !team.managerIDs.contains(account.id)) {
                Logger.getGlobal().info("Team not found or they have no access to edit it")
                call.respondText(
                    TeamUpdateFailResponse("Either that team doesn't exist or you don't have access to edit it").toJsonObject(),
                    ContentType.Application.Json,
                    HttpStatusCode.BadRequest
                )
                return@post
            }

            teamDAO.updateTeam(team.id, team.update(name, description, imageURL))

            Logger.getGlobal().info("Team updated successfully - ${team.id}")
            call.respondText(
                TeamUpdateSuccessResponse(team.id).toJsonObject(),
                ContentType.Application.Json,
            )

        }
    }
}