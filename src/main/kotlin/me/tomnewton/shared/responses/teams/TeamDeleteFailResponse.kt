package me.tomnewton.shared.responses.teams

import me.tomnewton.shared.responses.ErrorResponse

class TeamDeleteFailResponse(reason: String) : ErrorResponse(TEAM_DELETE_FAIL, reason) {
}