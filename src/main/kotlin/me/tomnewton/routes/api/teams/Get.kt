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
import java.util.logging.Logger

fun Route.getTeam(accountDAO: AccountDAO, teamDAO: TeamDAO) {
    authenticate("auth-jwt") {
        get("/") {
            // Return authenticated user's teams
            val principal = call.principal<JWTPrincipal>()!!
            val userID = principal.payload.getClaim("user_id").asLong()
            val teamArray = accountDAO.getAccountById(userID)!!.teamIDs.mapNotNull { teamDAO.getTeamById(it)?.toJsonObject() }
                .joinToString(",", "[", "]") { it }
            call.respondText(teamArray, ContentType.Application.Json, HttpStatusCode.Accepted)
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