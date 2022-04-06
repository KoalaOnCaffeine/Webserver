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
    val teamIDs: List<Int>,
    val projectIDs: List<Int>,
    val imageURL: String,
) : DataObject {

    override fun toJsonObject() = """ { "id": $id username: "$username", "email": "$email", "team_IDs": ${
        teamIDs.joinToString(
            prefix = "[", postfix = "]"
        )
    }, "project_IDs": ${projectIDs.joinToString(prefix = "[", postfix = "]")}, "image_url": $imageURL }"""

}
