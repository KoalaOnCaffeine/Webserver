package me.tomnewton.routes.api

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.routing.*
import me.tomnewton.ApplicationSettings
import me.tomnewton.database.AccountDAO
import me.tomnewton.database.TeamDAO
import me.tomnewton.routes.api.accounts.createAccount
import me.tomnewton.routes.api.accounts.getAccount
import me.tomnewton.routes.api.accounts.login
import me.tomnewton.routes.api.teams.getTeam
import java.util.*

/**
 * Registers all the routes under /api
 */

fun Route.apiRoutes(accountDAO: AccountDAO, teamDAO: TeamDAO) {
    route("/api") {
        route("/accounts") { accountRoutes(accountDAO) }
        route("/teams") { teamRoutes(accountDAO, teamDAO) }
    }
}

/**
 * Registers all account sub-routes
 */

private fun Route.accountRoutes(accountDAO: AccountDAO) {
    getAccount(accountDAO)
    createAccount(accountDAO)
    login(accountDAO)
}

private fun Route.teamRoutes(accountDAO: AccountDAO, teamDAO: TeamDAO) {
    getTeam(accountDAO, teamDAO)
}

/**
 * Creates a JWT string token for a given account id, using the application audience, issuer, and an expiry date 7 days from its issue date
 * @param accountID The account to create a claim to
 * @return the string token
 */
internal fun createTokenFor(accountID: Long): String {
    return JWT.create().withAudience(ApplicationSettings.audience).withIssuer(ApplicationSettings.issuer)
        .withClaim("user_id", accountID)
        .withExpiresAt(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // Expires after 7 days
        .sign(Algorithm.HMAC256("secret"))
}