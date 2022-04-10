package me.tomnewton.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import me.tomnewton.ApplicationSettings

fun Application.configureSecurity() {

    authentication {
        jwt("auth-jwt") {
            realm = ApplicationSettings.realm
            verifier(
                JWT.require(Algorithm.HMAC256("secret")) // Update this later
                    .withAudience(ApplicationSettings.audience).withIssuer(ApplicationSettings.issuer).build()
            )

            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") JWTPrincipal(credential.payload) else null
            }

            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is either invalid or has expired")
            }

        }
    }
}