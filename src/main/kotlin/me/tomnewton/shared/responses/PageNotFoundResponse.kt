package me.tomnewton.shared.responses

class PageNotFoundResponse : ErrorResponse(
    PAGE_NOT_FOUND, "This page does not exist"
)