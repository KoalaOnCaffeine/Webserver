package me.tomnewton.database

import me.tomnewton.database.model.Account

class AccountDAOImpl(private val accounts: MutableMap<Long, Account> = mutableMapOf()) : AccountDAO {

    // Create similar maps with the username and email as the keys
    private val accountsByUsername =
        accounts.map { entry -> entry.value.username to entry.value }.toMap().toMutableMap()

    private val accountsByEmail =
        accounts.map { entry -> entry.value.email to entry.value }.toMap().toMutableMap()

    override fun insertAccount(account: Account): Boolean {
        // If there was an account already, don't insert it
        if (accounts[account.id] != null || accountsByUsername[account.username] != null || accountsByEmail[account.email] != null) {
            return false
        }
        accounts[account.id] = account
        accountsByUsername[account.username] = account
        accountsByEmail[account.email] = account

        // TODO Remove - just to make every user have a default team of 0
        with(account) {
            updateAccount(
                id,
                Account(id, username, email, password, dateOfBirth, listOf(0, 1, 2, 3, 4, 5), projectIDs, imageURL)
            )
        }

        return true
    }

    override fun getAccountById(id: Long) = accounts[id]

    override fun getAccountByUsername(username: String) = accountsByUsername[username]

    override fun getAccountByEmail(email: String) = accountsByEmail[email]

    override fun updateAccount(id: Long, account: Account): Boolean {
        if (!accounts.containsKey(id)) return false // If the account isn't there, it cannot be updated
        accounts[id] = account
        accountsByUsername[account.username] = account
        accountsByEmail[account.email] = account
        return true // This operation just doesn't fail like a database could
    }
}