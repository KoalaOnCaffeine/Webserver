package me.tomnewton.routes.api.accounts

import me.tomnewton.database.AccountDAO
import me.tomnewton.shared.Account
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class CreateKtTest {

    private val elizabethOlsenAccount = Account("elizabeth_olsen", "", "", "", emptyList(), emptyList(), "")
    private val chrisHemsworthAccount = Account("chris_hemsworth", "", "", "", emptyList(), emptyList(), "")
    private val sebastianStanAccount = Account("sebastian_stan", "", "", "", emptyList(), emptyList(), "")

    private val exampleDAO: AccountDAO = ExampleAccountDAO(
        mutableMapOf(
            0 to elizabethOlsenAccount,
            1 to chrisHemsworthAccount
        )
    )

    @Test
    fun testCreateAbsentAccount() {
        assertTrue(exampleDAO.insertAccount(2, sebastianStanAccount))
    }

    @Test
    fun testCreatePresentAccount() {
        assertFalse(exampleDAO.insertAccount(0, sebastianStanAccount))
        assertFalse(exampleDAO.insertAccount(1, sebastianStanAccount))

        assertFalse(exampleDAO.insertAccount(0, elizabethOlsenAccount))
        assertFalse(exampleDAO.insertAccount(0, chrisHemsworthAccount))

        assertEquals(elizabethOlsenAccount, exampleDAO.getAccountById(0))
        assertEquals(chrisHemsworthAccount, exampleDAO.getAccountById(1))
    }

}