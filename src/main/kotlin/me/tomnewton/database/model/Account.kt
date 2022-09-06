package me.tomnewton.database.model

import me.tomnewton.shared.DataObject

/**
 * A data class for an account.
 * @param id The unique ID of the account
 * @param username The username of the account
 * @param email The email of the account
 * @param password The password of the account
 * @param dateOfBirth The date of birth of the owner of the account
 * @param teamIDs The IDs of the team this user is in
 * @param projectIDs The IDs of the projects this user is part of
 * @param imageURL The url of the user's profile picture
 */

data class Account(
    val id: Long,
    val username: String,
    val email: String,
    val password: String,
    val dateOfBirth: String,
    val teamIDs: List<Long>,
    val projectIDs: List<Long>,
    val imageURL: String,
) : DataObject {

    override fun toJsonObject() = """ { "id": $id, "username": "$username", "email": "$email", "team_IDs": ${
        teamIDs.joinToString(
            prefix = "[", postfix = "]"
        )
    }, "project_IDs": ${projectIDs.joinToString(prefix = "[", postfix = "]")}, "image_url": "$imageURL" }"""

}

fun Account.update(
    id: Long = this.id,
    username: String = this.username,
    email: String = this.email,
    password: String = this.password,
    dateOfBirth: String = this.dateOfBirth,
    teamIDs: List<Long> = this.teamIDs,
    projectIDs: List<Long> = this.projectIDs,
    imageURL: String = this.imageURL,
) = Account(id, username, email, password, dateOfBirth, teamIDs, projectIDs, imageURL)
