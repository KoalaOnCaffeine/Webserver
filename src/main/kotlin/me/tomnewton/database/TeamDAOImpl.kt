package me.tomnewton.database

import me.tomnewton.database.model.Team

class TeamDAOImpl(private val teams: MutableMap<Long, Team> = mutableMapOf()) : TeamDAO {

    override fun insertTeam(team: Team): Boolean {
        // Return true if the previous value was null, meaning nothing would be overwritten
        val absent = teams.putIfAbsent(team.id, team)
        return absent == null
    }

    override fun getTeamById(id: Long) = teams[id]

    override fun updateTeam(id: Long, team: Team): Boolean {
        teams[id] = team
        return true // todo check if there was an account
    }
}