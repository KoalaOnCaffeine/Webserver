package me.tomnewton.shared.responses.teams

import me.tomnewton.shared.responses.ErrorResponse

class TeamUpdateFailResponse(reason: String) : ErrorResponse(TEAM_UPDATE_FAIL, reason) {
}