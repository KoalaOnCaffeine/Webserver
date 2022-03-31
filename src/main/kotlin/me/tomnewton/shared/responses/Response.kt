package me.tomnewton.shared.responses

import me.tomnewton.shared.DataObject

open class Response(
    private val code: Int, private val message: String = "Response code :$code", private val data: String = "{}"
) : DataObject {
    override fun toJsonObject() = """{code: $code, message: "$message", data: $data}"""
}