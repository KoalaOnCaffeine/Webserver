package me.tomnewton.routes.api.accounts

/**
 * A data class for an account returned from the API
 * Accounts returned from the API should not have the associated email, password, or date of birth
 * @param id The ID of the account
 * @param username The username of the account
 */

data class Account(
    val id: Int,
    var username: String
)
