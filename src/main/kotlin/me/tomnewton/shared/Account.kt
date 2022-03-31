package me.tomnewton.shared

/**
 * A data class for an account
 * Accounts returned from the API should not have the associated email, password, or date of birth, so they should be removed before sending
 * @param username The username of the account
 * @param email The email of the account
 * @param password The password of the account
 * @param dateOfBirth The date of birth of the owner of the account
 */

data class Account(
    val username: String,
    val email: String,
    val password: String,
    val dateOfBirth: String,
    val teamIDs: List<Int>,
    val projectIDs: List<Int>,
    val imageURL: String,
) : DataObject {

    override fun toJsonObject() = """
            {
                username: "$username",
                email: "$email",
                team_ids: ${teamIDs.joinToString(prefix = "[", postfix = "]")},
                project_ids: ${projectIDs.joinToString(prefix = "[", postfix = "]")},
                image_url: $imageURL
            }""".trimIndent()

}
