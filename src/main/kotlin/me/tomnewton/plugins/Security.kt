package me.tomnewton.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import me.tomnewton.ApplicationSettings

fun Application.configureSecurity() {

    authentication {
        jwt {
            realm = ApplicationSettings.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256("secret"))
                    .withAudience(ApplicationSettings.audience)
                    .withIssuer(ApplicationSettings.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(ApplicationSettings.audience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}