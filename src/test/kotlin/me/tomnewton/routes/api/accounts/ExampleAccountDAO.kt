package me.tomnewton.routes.api.accounts

class ExampleAccountDAO(private val accounts: MutableMap<Int, Account> = mutableMapOf()) : AccountDAO {

    override fun insertAccount(id: Int, account: Account): Boolean {
        // Return true if the previous value was null, meaning nothing would be overwritten
        return accounts.putIfAbsent(id, account) == null
    }

    override fun getAccountById(id: Int) = accounts[id]

    override fun updateAccount(id: Int, account: Account): Boolean {
        accounts[id] = account
        return true // This operation just doesn't fail like a database could
    }
}