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
import me.tomnewton.shared.responses.teams.TeamDeleteFailResponse
import me.tomnewton.shared.responses.teams.TeamDeleteSuccessResponse
import java.util.logging.Logger

fun Route.deleteTeam(accountDAO: AccountDAO, teamDAO: TeamDAO) {
    authenticate("auth-jwt") {
        post("/delete") {
            val principal = call.principal<JWTPrincipal>()!!
            val userID = principal.payload.getClaim("user_id").asLong()
            val account = accountDAO.getAccountById(userID)!! // Account cannot be null passed authentication
            val content = parseObject(call.receiveText())

            val teamID = content["teamID"]?.toString()?.toLongOrNull()

            // You must provide an ID for the team to delete
            if (teamID == null) {
                Logger.getGlobal().info("Team delete fail - no team id provided")
                call.respondText(
                    TeamDeleteFailResponse("Must provide a teamID field").toJsonObject(),
                    ContentType.Application.Json,
                    HttpStatusCode.BadRequest
                )
                return@post
            }

            val team = teamDAO.getTeamById(teamID)

            // If the team doesn't exist, it can't be deleted
            if (team == null) {
                Logger.getGlobal().info("Team delete fail - no team found")
                call.respondText(
                    TeamDeleteFailResponse("A team with that ID was not found").toJsonObject(),
                    ContentType.Application.Json,
                    HttpStatusCode.BadRequest
                )
                return@post
            }

            // If the user requesting was not a manager, they can't delete the team
            if (!team.managerIDs.contains(account.id)) {
                Logger.getGlobal().info("Team delete fail - no permission")
                call.respondText(
                    TeamDeleteFailResponse("You do not have permission to delete that team").toJsonObject(),
                    ContentType.Application.Json,
                    HttpStatusCode.BadRequest
                )
                return@post
            }

            // Delete the team since they are allowed to, and it exists
            (team.managerIDs + team.memberIDs).map(accountDAO::getAccountById).forEach {
                if (it != null) accountDAO.updateAccount(it.id, it.update(teamIDs = it.teamIDs - team.id))
            }
            teamDAO.removeTeam(teamID)

            Logger.getGlobal().info("Team deleted successfully - $teamID")
            call.respondText(
                TeamDeleteSuccessResponse(teamID).toJsonObject(),
                ContentType.Application.Json,
            )

        }
    }
}