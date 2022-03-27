package me.tomnewton.routes.api.accounts

import me.tomnewton.routes.DataObject

/**
 * A data class for an account returned from the API
 * Accounts returned from the API should not have the associated email, password, or date of birth
 * @param username The username of the account
 */

data class Account(
    var username: String
) : DataObject {
    override fun toJsonObject() = """
        {\"username\": \"${username}\"}
    """.trimIndent()
}
