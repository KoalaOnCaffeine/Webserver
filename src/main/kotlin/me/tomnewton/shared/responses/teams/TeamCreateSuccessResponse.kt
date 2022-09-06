package me.tomnewton.shared.responses.teams

import me.tomnewton.shared.responses.Response

class TeamCreateSuccessResponse(teamID: Long) : Response(
    TEAM_CREATE_SUCCESS, "Team created successfully", "{\"id\": $teamID}"
)