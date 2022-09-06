package me.tomnewton

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.tomnewton.database.AccountDAOImpl
import me.tomnewton.database.TeamDAOImpl
import me.tomnewton.plugins.configureHTTP
import me.tomnewton.plugins.configureRouting
import me.tomnewton.plugins.configureSecurity
import me.tomnewton.plugins.configureStatusPages

fun main() {
    val accountDAO = AccountDAOImpl()
    val teamDAO = TeamDAOImpl()

    embeddedServer(Netty, port = 8080, host = ApplicationSettings.domain) {
        configureSecurity()
        configureRouting(accountDAO, teamDAO)
        configureHTTP()
        configureStatusPages()
    }.start(wait = true)
}