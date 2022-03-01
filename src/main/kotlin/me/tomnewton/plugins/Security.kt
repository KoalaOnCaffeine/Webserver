package me.tomnewton.plugins

import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import me.tomnewton.ApplicationSettings

fun Application.configureSecurity() {

    authentication {
        jwt {
            val jwtAudience = "tomnewton.me"
            realm = ApplicationSettings.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256("secret"))
                    .withAudience(ApplicationSettings.audience)
                    .withIssuer(ApplicationSettings.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}