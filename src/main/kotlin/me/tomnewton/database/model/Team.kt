package me.tomnewton.database.model

import me.tomnewton.routes.api.accounts.defaultImage
import me.tomnewton.shared.DataObject

/**
 * A data class for a team.
 * @param id The unique ID of the team
 * @param description The description of the team
 * @param projectIDs The projects associated with this team
 * @param managerIDs The managers of this team
 * @param memberIDs The members in the team
 * @param imageURL The url of the team's picture
 */

data class Team(
    val id: Long,
    val name: String,
    val description: String?,
    val projectIDs: List<Long>,
    val managerIDs: List<Long>,
    val memberIDs: List<Long>,
    val imageURL: String?
) : DataObject {
    override fun toJsonObject() =
        """{ "id": "$id", "name": "$name", "description": ${if (description == null) null else "\"$description\""}, "projectIDs": ${
            projectIDs.joinToString(
                prefix = "[", postfix = "]"
            )
        }, "managerIDs": ${
            managerIDs.joinToString(prefix = "[", postfix = "]", transform = { "\"$it\"" })
        }, "memberIDs": ${
            memberIDs.joinToString(prefix = "[", postfix = "]", transform = { "\"$it\"" })
        }, "imageURL": "${if (imageURL == null) defaultImage else "$imageURL"}" }"""
}

fun Team.update(
    name: String? = null,
    description: String? = null,
    imageURL: String? = null
) = Team(
    this.id,
    name ?: this.name,
    description ?: this.description,
    this.projectIDs,
    this.managerIDs,
    this.memberIDs,
    imageURL ?: this.imageURL
)
