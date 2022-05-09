package me.tomnewton.routes.api.accounts

import me.tomnewton.database.AccountDAO
import me.tomnewton.database.AccountDAOTestImpl
import me.tomnewton.database.model.Account
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class GetKtTest {

    private val elizabethOlsenAccount =
        Account(System.nanoTime(), "elizabeth_olsen", "", "", "", emptyList(), emptyList(), "")
    private val chrisHemsworthAccount =
        Account(System.nanoTime(), "chris_hemsworth", "", "", "", emptyList(), emptyList(), "")

    private val exampleDAO: AccountDAO = AccountDAOTestImpl(
        mutableMapOf(
            0L to elizabethOlsenAccount,
            1L to chrisHemsworthAccount
        )
    )

    @Test
    fun testGetPresentAccount() {
        assertEquals(elizabethOlsenAccount, exampleDAO.getAccountById(0))
        assertEquals(chrisHemsworthAccount, exampleDAO.getAccountById(1))
    }

    @Test
    fun testGetAbsentAccount() {
        assertEquals(null, exampleDAO.getAccountById(-1))
        assertEquals(null, exampleDAO.getAccountById(2))
    }

}