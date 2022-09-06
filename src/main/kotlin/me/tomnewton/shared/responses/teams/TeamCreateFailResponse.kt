package me.tomnewton.shared.responses.teams

import me.tomnewton.shared.responses.ErrorResponse

class TeamCreateFailResponse : ErrorResponse(TEAM_CREATE_FAIL, "Must provide a team name field") {
}