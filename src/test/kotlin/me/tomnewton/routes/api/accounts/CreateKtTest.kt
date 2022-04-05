package me.tomnewton.routes.api.accounts

import me.tomnewton.database.AccountDAO
import me.tomnewton.database.AccountDAOImpl
import me.tomnewton.database.model.Account
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class CreateKtTest {

    private val elizabethOlsenAccount =
        Account(System.nanoTime(), "elizabeth_olsen", "", "", "", emptyList(), emptyList(), "")
    private val chrisHemsworthAccount =
        Account(System.nanoTime(), "chris_hemsworth", "", "", "", emptyList(), emptyList(), "")
    private val sebastianStanAccount =
        Account(System.nanoTime(), "sebastian_stan", "", "", "", emptyList(), emptyList(), "")

    @Test
    fun testCreateAbsentAccount() {
        val exampleDAO: AccountDAO = AccountDAOImpl(
            mutableMapOf(
                elizabethOlsenAccount.id to elizabethOlsenAccount, chrisHemsworthAccount.id to chrisHemsworthAccount
            )
        )
        assertTrue(exampleDAO.insertAccount(sebastianStanAccount))
    }

    @Test
    fun testCreatePresentAccount() {
        val exampleDAO: AccountDAO = AccountDAOImpl(
            mutableMapOf(
                elizabethOlsenAccount.id to elizabethOlsenAccount, chrisHemsworthAccount.id to chrisHemsworthAccount
            )
        )
        assertTrue(exampleDAO.insertAccount(sebastianStanAccount)) // Able to insert account
        assertFalse(exampleDAO.insertAccount(sebastianStanAccount)) // Unable to insert duplicate

        assertFalse(exampleDAO.insertAccount(elizabethOlsenAccount)) // Unable to insert duplicate
        assertFalse(exampleDAO.insertAccount(chrisHemsworthAccount)) // Unable to insert duplicate

        assertEquals(elizabethOlsenAccount, exampleDAO.getAccountById(elizabethOlsenAccount.id)) // Correct account
        assertEquals(chrisHemsworthAccount, exampleDAO.getAccountById(chrisHemsworthAccount.id)) // Correct account
    }

}