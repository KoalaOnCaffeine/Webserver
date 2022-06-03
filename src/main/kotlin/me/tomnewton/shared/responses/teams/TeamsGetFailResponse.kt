package me.tomnewton.shared.responses.teams

import me.tomnewton.shared.responses.ErrorResponse

class TeamsGetFailResponse(reason: String) : ErrorResponse(TEAMS_GET_FAIL, reason)