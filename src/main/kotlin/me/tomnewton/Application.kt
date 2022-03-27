package me.tomnewton

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.tomnewton.database.AccountDAOImpl
import me.tomnewton.plugins.configureHTTP
import me.tomnewton.plugins.configureRouting
import me.tomnewton.plugins.configureSecurity
import me.tomnewton.plugins.configureStatusPages

fun main() {

    val accountDAO = AccountDAOImpl()

    embeddedServer(Netty, port = 8080, host = "localhost") {
        configureRouting(accountDAO)
        configureSecurity()
        configureHTTP()
        configureStatusPages()
    }.start(wait = true)
}

object Messages {
    const val notFoundMessage: String = "{\"message\": \"This page does not exist\"}"
}