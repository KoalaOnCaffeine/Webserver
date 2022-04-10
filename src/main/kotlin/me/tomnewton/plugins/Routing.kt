package me.tomnewton.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.tomnewton.database.AccountDAO
import me.tomnewton.routes.api.apiRoutes
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

fun Application.configureRouting(accountDAO: AccountDAO) {

    routing {
        apiRoutes(accountDAO)
        get("/") {
            call.respondText("Hello World!")
        }
    }
}

fun ApplicationCall.parameter(name: String): String? {
    return parameters[name]
}

fun <T> ApplicationCall.parameter(name: String, transform: (String) -> T?): T? {
    val value = parameter(name)
    return if (value == null) null
    else transform(value)
}

fun parseObject(json: String): JSONObject {
    val parser = JSONParser()
    return parser.parse(json) as JSONObject
}