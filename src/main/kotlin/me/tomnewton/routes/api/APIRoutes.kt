package me.tomnewton.routes.api

import io.ktor.server.routing.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.routes.api.accounts.createAccount
import me.tomnewton.routes.api.accounts.getAccount

/**
 * Registers all the routes under /api
 */

fun Route.apiRoutes(accountDAO: AccountDAO) {
    route("/api") {
        route("/accounts") { accountRoutes(accountDAO) }
    }
}

/**
 * Registers all account sub-routes
 */

private fun Route.accountRoutes(accountDAO: AccountDAO) {
    getAccount(accountDAO)
    createAccount(accountDAO)
}