package me.tomnewton

object ApplicationSettings {
    // Intended audience who should use the JWT token
    const val audience: String = "localhost"

    // The purpose of the JWT token
    const val realm: String = "Authentication"

    // Who created the JWT
    const val issuer: String = "localhost"
}