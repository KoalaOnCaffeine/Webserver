package me.tomnewton.routes.api.accounts

import me.tomnewton.database.AccountDAO
import me.tomnewton.shared.Account
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class GetKtTest {

    private val elizabethOlsenAccount = Account("elizabeth_olsen")
    private val chrisHemsworthAccount = Account("chris_hemsworth")

    private val exampleDAO: AccountDAO = ExampleAccountDAO(
        mutableMapOf(
            0 to elizabethOlsenAccount,
            1 to chrisHemsworthAccount
        )
    )

    @Test
    fun getPresentAccountTest() {
        assertEquals(elizabethOlsenAccount, exampleDAO.getAccountById(0))
        assertEquals(chrisHemsworthAccount, exampleDAO.getAccountById(1))
    }

    @Test
    fun getAbsentAccountTest() {
        assertEquals(null, exampleDAO.getAccountById(-1))
        assertEquals(null, exampleDAO.getAccountById(2))
    }

}