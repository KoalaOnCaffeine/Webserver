package me.tomnewton.database

import me.tomnewton.database.model.Account

class AccountDAOTestImpl(private val accounts: MutableMap<Long, Account> = mutableMapOf()) : AccountDAO {

    override fun countAccounts() = accounts.size

    override fun insertAccount(account: Account): Boolean {
        // Return true if the previous value was null, meaning nothing would be overwritten
        return accounts.putIfAbsent(account.id, account) == null
    }

    override fun getAccountById(id: Long) = accounts[id]

    override fun updateAccount(id: Long, account: Account): Boolean {
        accounts[id] = account
        return true // This operation just doesn't fail like a database could
    }
}