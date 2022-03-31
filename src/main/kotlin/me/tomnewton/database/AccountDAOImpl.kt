package me.tomnewton.database

import me.tomnewton.shared.Account
import java.util.concurrent.ThreadLocalRandom

class AccountDAOImpl(private val accounts: MutableMap<Int, Account> = mutableMapOf()) : AccountDAO {

    private var nextID = 0

    override fun countAccounts() = accounts.size

    override fun insertAccount(account: Account): Boolean {

        // Just give it a 50% chance to fail for the lols
        if (ThreadLocalRandom.current().nextBoolean()) return false

        // Return true if the previous value was null, meaning nothing would be overwritten
        return accounts.putIfAbsent(nextID++, account) == null
    }

    override fun getAccountById(id: Int) = accounts[id]

    override fun updateAccount(id: Int, account: Account): Boolean {
        accounts[id] = account
        return true // This operation just doesn't fail like a database could
    }
}