package me.tomnewton.database

import me.tomnewton.database.model.Team

interface TeamDAO {

    fun insertTeam(team: Team): Boolean

    fun getTeamById(id: Long): Team?

    fun updateTeam(id: Long, team: Team): Boolean

    fun removeTeam(id: Long): Boolean

}