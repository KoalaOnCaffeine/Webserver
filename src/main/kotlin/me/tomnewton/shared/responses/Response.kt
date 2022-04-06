package me.tomnewton.shared.responses

import me.tomnewton.shared.DataObject

val EMPTY_RESPONSE = Response(-1)

open class Response(
    private val code: Int, private val message: String = "", private val data: String? = null
) : DataObject {
    override fun toJsonObject() = """{code: $code, message: "$message"${if (data == null) "" else ", data: $data"}}"""
}