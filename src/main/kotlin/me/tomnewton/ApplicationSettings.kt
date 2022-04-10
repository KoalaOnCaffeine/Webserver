package me.tomnewton

object ApplicationSettings {
    // Intended audience who should use the JWT token
    const val audience: String = "http://localhost:8080/auth"

    // The purpose of the JWT token
    const val realm: String = "Authentication"

    // Who created the JWT
    const val issuer: String = "http://localhost:8080/"
}