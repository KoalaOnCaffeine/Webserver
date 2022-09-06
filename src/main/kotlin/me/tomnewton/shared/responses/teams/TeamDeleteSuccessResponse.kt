package me.tomnewton.shared.responses.teams

import me.tomnewton.shared.responses.Response

class TeamDeleteSuccessResponse(teamID: Long) : Response(
    TEAM_DELETE_SUCCESS, "Team deleted successfully", "{\"id\": $teamID}"
)