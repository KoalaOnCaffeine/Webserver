package me.tomnewton.shared.responses.teams

import me.tomnewton.shared.responses.Response

class TeamUpdateSuccessResponse(teamID: Long) : Response(
    TEAM_UPDATE_SUCCESS, "Team successfully updated", "{\"id\": $teamID}"
)