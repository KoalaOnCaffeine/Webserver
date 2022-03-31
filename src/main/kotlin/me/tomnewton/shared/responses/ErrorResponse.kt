package me.tomnewton.shared.responses

/**
 * A typical error response, with a given code and error. The message is "An error occurred", and the specific message is under data.error
 * @param code The code of the error
 * @param errorDescription A description of the error
 */

open class ErrorResponse(code: Int, errorDescription: String) :
    Response(code, "An error occurred", "{error: \"$errorDescription\"}")