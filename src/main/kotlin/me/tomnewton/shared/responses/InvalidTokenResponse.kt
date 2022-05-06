package me.tomnewton.shared.responses

class InvalidTokenResponse : ErrorResponse(INVALID_TOKEN, "Token is either invalid or expired")