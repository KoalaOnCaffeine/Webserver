package me.tomnewton.database

import me.tomnewton.database.model.Account

class AccountDAOImpl(private val accounts: MutableMap<Long, Account> = mutableMapOf()) : AccountDAO {

    // Create a similar map with the username as the key
    private val accountsByUsername =
        accounts.map { entry -> entry.value.username to entry.value }.toMap().toMutableMap()

    override fun insertAccount(account: Account): Boolean {
        // Return true if the previous value was null, meaning nothing would be overwritten
        val absent = accounts.putIfAbsent(account.id, account)
        if (absent == null) {
            accountsByUsername[account.username] = account
            // TODO remove - creates a team with ID 0
            with(account) {
                updateAccount(id, Account(id, username, email, password, dateOfBirth, listOf(0), projectIDs, imageURL))
            }
            return true
        }
        return false
    }

    override fun getAccountById(id: Long) = accounts[id]

    override fun getAccountByUsername(username: String) = accountsByUsername[username]

    override fun updateAccount(id: Long, account: Account): Boolean {
        if (!accounts.containsKey(id)) return false // If the account isn't there, it cannot be updated
        accounts[id] = account
        accountsByUsername[account.username] = account
        return true // This operation just doesn't fail like a database could
    }
}