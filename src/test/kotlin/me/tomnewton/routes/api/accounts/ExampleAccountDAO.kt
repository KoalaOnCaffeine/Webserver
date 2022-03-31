package me.tomnewton.routes.api.accounts

import me.tomnewton.database.AccountDAO
import me.tomnewton.shared.Account

class ExampleAccountDAO(private val accounts: MutableMap<Int, Account> = mutableMapOf()) : AccountDAO {

    private var nextID = 0

    override fun countAccounts(): Int {
        return accounts.size
    }

    override fun insertAccount(account: Account): Boolean {
        // Return true if the previous value was null, meaning nothing would be overwritten
        return accounts.putIfAbsent(nextID++, account) == null
    }

    override fun getAccountById(id: Int) = accounts[id]

    override fun updateAccount(id: Int, account: Account): Boolean {
        accounts[id] = account
        return true // This operation just doesn't fail like a database could
    }
}