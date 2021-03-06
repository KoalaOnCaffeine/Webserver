package me.tomnewton.routes.api.accounts

import io.ktor.http.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.database.AccountDAOImpl
import me.tomnewton.database.TeamDAO
import me.tomnewton.database.TeamDAOImpl
import me.tomnewton.database.model.Account
import me.tomnewton.routes.test
import me.tomnewton.shared.responses.accounts.ACCOUNT_GET_FAIL
import me.tomnewton.shared.responses.accounts.ACCOUNT_GET_SUCCESS
import org.junit.Test

internal val elizabethOlsenAccount = Account(
    System.currentTimeMillis(),
    validUsername,
    validEmail,
    validPassword,
    validDateOfBirth,
    mutableListOf(),
    mutableListOf(),
    defaultImage
)

internal val filledAccountDAO = AccountDAOImpl(mutableMapOf(elizabethOlsenAccount.id to elizabethOlsenAccount))

class GetKtTest {

    private fun expect(
        id: String,
        expectedCode: Int,
        expectedStatusCode: HttpStatusCode = HttpStatusCode.OK,
        expectedContentType: ContentType = ContentType.Application.Json,
        accountDAO: AccountDAO = AccountDAOImpl(),
        teamDAO: TeamDAO = TeamDAOImpl(),
    ) {
        test(
            HttpMethod.Get,
            "/api/accounts/$id",
            expectedCode,
            expectedStatusCode,
            expectedContentType,
            accountDAO,
            teamDAO,
        )
    }

    @Test
    fun testGetPresentAccount() {
        expect(
            elizabethOlsenAccount.id.toString(), ACCOUNT_GET_SUCCESS, HttpStatusCode.OK, accountDAO = filledAccountDAO
        )
    }

    @Test
    fun testGetAbsentAccount() {
        expect("0", ACCOUNT_GET_FAIL, HttpStatusCode.NoContent)
    }

    @Test
    fun testGetInvalidAccountID() {
        expect("-=very invalid id=-", ACCOUNT_GET_FAIL, HttpStatusCode.BadRequest)
    }

}