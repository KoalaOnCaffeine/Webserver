package me.tomnewton.routes.api.teams

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.database.TeamDAO
import me.tomnewton.plugins.parameter
import me.tomnewton.shared.responses.teams.TeamsGetFailResponse
import me.tomnewton.shared.responses.teams.TeamsGetSuccessResponse
import java.util.logging.Logger

fun Route.getTeam(accountDAO: AccountDAO, teamDAO: TeamDAO) {
    authenticate("auth-jwt") {
        get("/") {
            // Return authenticated user's teams
            val principal = call.principal<JWTPrincipal>()!!
            val userID = principal.payload.getClaim("user_id").asLong()
            val account = accountDAO.getAccountById(userID)

            if (account == null) {
                // Token didn't have a claim to a valid account - tell them the token was invalid
                Logger.getGlobal().info("Teams requested with invalid token")
                val response = TeamsGetFailResponse("The provided token was invalid")
                call.respondText(response.toJsonObject(), ContentType.Application.Json, HttpStatusCode.BadRequest)
            } else {
                // Token had a claim to a valid account - return their teams
                Logger.getGlobal().info("Teams found")
                val response = TeamsGetSuccessResponse(account.teamIDs.mapNotNull(teamDAO::getTeamById))
                call.respondText(response.toJsonObject(), ContentType.Application.Json, HttpStatusCode.OK)
            }
        }
        get("/{id}") {
            val principal = call.principal<JWTPrincipal>()!!
            val userID = principal.payload.getClaim("user_id").asLong()
            val account = accountDAO.getAccountById(userID)!! // todo null
            val teamID = call.parameter("id")!!.toLongOrNull()!! // todo double null
            if (account.teamIDs.contains(teamID)) {
                Logger.getGlobal().info("Team found")
                val team = teamDAO.getTeamById(teamID)
                if (team == null) {
                    Logger.getGlobal().info("Team found but like not found")
                    call.respondText("No team with that ID exists")
                } else {
                    Logger.getGlobal().info("Team found")
                    call.respondText(team.toJsonObject())
                }
            } else {
                Logger.getGlobal().info("Team not found")
                call.respondText("The team either doesn't exist or you do not have access")
            }
        }
    }
}