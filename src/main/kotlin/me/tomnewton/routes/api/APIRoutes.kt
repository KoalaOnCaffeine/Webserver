package me.tomnewton.routes.api

import io.ktor.server.routing.*
import me.tomnewton.routes.api.accounts.createAccount
import me.tomnewton.routes.api.accounts.getAccount

/**
 * Registers all the routes under /api
 */

fun Route.apiRoutes() {
    route("/api") {
        route("/accounts", Route::accountRoutes)
    }
}

/**
 * Registers all account sub-routes
 */

private fun Route.accountRoutes() {
    getAccount()
    createAccount()
}