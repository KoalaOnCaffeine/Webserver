package me.tomnewton.database

import me.tomnewton.shared.Account

/*
https://www.tutorialspoint.com/design_pattern/data_access_object_pattern.htm
https://stackoverflow.com/questions/19154202/data-access-object-dao-in-java
 */
interface AccountDAO {

    /**
     * Gets the number of accounts in the database
     * @return the number of accounts
     */

    fun countAccounts(): Int

    /**
     * Inserts an account into the records, auto-incrementing the ID
     * @param account the account to add
     * @return Whether the operation was successful
     */

    fun insertAccount(account: Account): Boolean

    /**
     * Get an account with a specified [id]
     * @param id the id of the account
     * @return The associated account else null
     */

    fun getAccountById(id: Int): Account?

    /**
     * Update the given [account] in the database
     * @param id the id of the account to update
     * @param account The account to update
     * @return Whether the operation was successful
     */

    fun updateAccount(id: Int, account: Account): Boolean
}