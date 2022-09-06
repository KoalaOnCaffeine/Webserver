package me.tomnewton.shared.responses.teams

import me.tomnewton.database.model.Team
import me.tomnewton.shared.responses.Response

class TeamsGetSuccessResponse(teams: List<Team>) : Response(TEAMS_GET_SUCCESS, data = """{"teams": ${
    teams.joinToString(prefix = "[", postfix = "]") {
        it.toJsonObject().also { println(it) }
    }
} }""")