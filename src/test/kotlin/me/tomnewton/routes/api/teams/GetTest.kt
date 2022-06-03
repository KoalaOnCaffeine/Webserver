package me.tomnewton.routes.api.teams

import io.ktor.client.request.*
import io.ktor.http.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.database.AccountDAOImpl
import me.tomnewton.routes.api.accounts.elizabethOlsenAccount
import me.tomnewton.routes.api.accounts.filledAccountDAO
import me.tomnewton.routes.api.createTokenFor
import me.tomnewton.routes.test
import me.tomnewton.shared.responses.INVALID_TOKEN
import me.tomnewton.shared.responses.teams.TEAMS_GET_FAIL
import me.tomnewton.shared.responses.teams.TEAMS_GET_SUCCESS
import org.junit.Test

class GetTest {

    private fun expect(
        token: String?,
        expectedCode: Int,
        expectedStatusCode: HttpStatusCode = HttpStatusCode.OK,
        accountDAO: AccountDAO = AccountDAOImpl(),
    ) {
        test(HttpMethod.Get, "/api/teams/", expectedCode, expectedStatusCode, accountDAO = accountDAO, builder = {
            if (token == null) header("Authorization", "Bearer ")
            else header("Authorization", "Bearer $token")
        })
    }

    @Test
    fun testValidTokenGet() {
        // valid token for a present account
        val token = createTokenFor(elizabethOlsenAccount.id)
        expect(token, TEAMS_GET_SUCCESS, HttpStatusCode.OK, filledAccountDAO)
    }

    @Test
    fun testInvalidTokenClaimGet() {
        // invalid token, but one that can still be parsed - this account id will never exist
        expect(createTokenFor(Long.MIN_VALUE), TEAMS_GET_FAIL, HttpStatusCode.BadRequest, filledAccountDAO)
    }

    @Test
    fun testInvalidTokenGet() {
        // invalid token - should be unauthorized
        expect("-=very invalid token=-", INVALID_TOKEN, HttpStatusCode.Unauthorized, filledAccountDAO)
    }

    @Test
    fun testNoTokenGet() {
        // no token provided - should be unauthorized
        expect(null, INVALID_TOKEN, HttpStatusCode.Unauthorized, filledAccountDAO)
    }

}