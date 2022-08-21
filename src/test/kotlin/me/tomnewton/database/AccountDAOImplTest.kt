package me.tomnewton.database

import me.tomnewton.database.model.Account
import me.tomnewton.routes.api.accounts.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertNull
import kotlin.test.assertEquals

private val elizabethOlsenAccount = Account(
    0, "elizabeth_olsen", validPassword, validPassword, validDateOfBirth, mutableListOf(), mutableListOf(), defaultImage
)
private val updatedElizabethOlsenAccount = Account(
    1, validUsername, validEmail, validPassword, validDateOfBirth, mutableListOf(), mutableListOf(), defaultImage
)

class AccountDAOImplTest {
    @Test
    fun testInsertAccount() {
        val testAccountDAO = AccountDAOImpl()
        assertTrue(testAccountDAO.insertAccount(elizabethOlsenAccount))
    }

    @Test
    fun testInsertDuplicateAccount() {
        val testAccountDAO = AccountDAOImpl(mutableMapOf(elizabethOlsenAccount.id to elizabethOlsenAccount))
        assertFalse(testAccountDAO.insertAccount(elizabethOlsenAccount))
    }

    @Test
    fun testGetPresentAccountByID() {
        val testAccountDAO = AccountDAOImpl(mutableMapOf(elizabethOlsenAccount.id to elizabethOlsenAccount))
        assertEquals(testAccountDAO.getAccountById(elizabethOlsenAccount.id), elizabethOlsenAccount)
    }

    @Test
    fun testGetMissingAccountByID() {
        val testAccountDAO = AccountDAOImpl()
        testAccountDAO.insertAccount(elizabethOlsenAccount)
        assertNull(testAccountDAO.getAccountById(-1))
    }

    @Test
    fun testGetPresentAccountByUsername() {
        val testAccountDAO = AccountDAOImpl(mutableMapOf(elizabethOlsenAccount.id to elizabethOlsenAccount))
        assertEquals(testAccountDAO.getAccountByUsername(elizabethOlsenAccount.username), elizabethOlsenAccount)
    }

    @Test
    fun testGetMissingAccountByUsername() {
        val testAccountDAO = AccountDAOImpl(mutableMapOf(elizabethOlsenAccount.id to elizabethOlsenAccount))
        assertNull(testAccountDAO.getAccountByUsername("chris_hemsworth"))
    }

    @Test
    fun testUpdateAccount() {
        val testAccountDAO = AccountDAOImpl(mutableMapOf(elizabethOlsenAccount.id to elizabethOlsenAccount))
        assertTrue(testAccountDAO.updateAccount(elizabethOlsenAccount.id, updatedElizabethOlsenAccount))
    }

    @Test
    fun testUpdateAccountWhenNotPresent() {
        val testAccountDAO = AccountDAOImpl()
        assertFalse(testAccountDAO.updateAccount(elizabethOlsenAccount.id, updatedElizabethOlsenAccount))
    }
}