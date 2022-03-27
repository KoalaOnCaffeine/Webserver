package me.tomnewton.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.tomnewton.routes.api.apiRoutes
import kotlin.reflect.KProperty

fun Application.configureRouting() {

    routing {
        apiRoutes()
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

operator fun ApplicationCall.getValue(ref: Any?, property: KProperty<*>): String? {
    return parameter(property.name)
}