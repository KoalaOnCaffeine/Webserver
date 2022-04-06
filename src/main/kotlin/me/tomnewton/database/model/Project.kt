package me.tomnewton.database.model

import me.tomnewton.shared.DataObject

/**
 * A data class for a project.
 * @param id THe unique ID of the project
 * @param description The description of the project
 * @param managerIDs The managers associated with the team
 * @param memberIDs The members associated with the team
 * @param archived The archived state of the project
 * @param cardIDs The cards associated with the project
 * @param imageURL The url of the project's picture
 */

data class Project(
    val id: Long,
    val description: String?,
    val managerIDs: List<Long>,
    val memberIDs: List<Long>,
    val imageURL: String,
    val archived: Boolean,
    val cardIDs: List<Long>
) : DataObject {
    override fun toJsonObject() = """ { "id": $id, "description": "$description", "managerIDs": ${
        managerIDs.joinToString(
            prefix = "[",
            postfix = "]"
        )
    }, "memberIDs": ${
        memberIDs.joinToString(
            prefix = "[",
            postfix = "]"
        )
    }, "imageURL": "$imageURL", "archived": $archived, "cardIDs": ${cardIDs.joinToString(prefix = "[", postfix = "]")} } """
}