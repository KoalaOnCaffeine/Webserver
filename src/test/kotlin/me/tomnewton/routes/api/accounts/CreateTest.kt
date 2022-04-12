package me.tomnewton.routes.api.accounts

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import me.tomnewton.plugins.parseObject
import me.tomnewton.routes.test
import me.tomnewton.shared.responses.accounts.ACCOUNT_CREATE_SUCCESS
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.assertContains
import kotlin.test.assertEquals

internal const val validUsername = "elizabeth_olsen"
internal const val usernameTooLong = "elizabeth_olsen_wanda" // > 20 chars
internal const val usernameInvalidChars = "elizabeth-olsen" // _ is invalid

internal const val validEmail = "elizabeth@olsen.co.po"
internal const val emailNoDomain = "elizabeth@"
internal const val emailInvalidDomain = "elizabeth@co"
internal const val emailJustUser = "elizabeth"
internal const val emailJustDomain = "@co.po"

internal const val validPassword = "P0!INTBr3ak!"
internal const val passwordTooShort = "W4nda!" //


internal const val validDateOfBirth = "16/02/1989"
internal val dateOfBirthTooYoung = SimpleDateFormat("dd/MM/yyyy").format(Date()) // Current date is too young

class CreateTest {

    private fun postWith(
        username: String,
        email: String,
        password: String,
        dateOfBirth: String,
        builder: HttpRequestBuilder.() -> Unit = {},
        response: suspend HttpResponse.() -> Unit
    ) {
        test(HttpMethod.Post, "/api/accounts/create", {
            builder(this)
            setBody("""{"username": "$username", "email": "$email", "password": "$password", "dateOfBirth" : "$dateOfBirth"}""")
        }) { runBlocking { response(this@test) } }
    }

    @Test
    fun testAllValid() {
        postWith(validUsername, validEmail, validPassword, validDateOfBirth) {
            assertContains(contentType()?.contentType ?: "", ContentType.Application.Json.contentType)
            assertEquals(HttpStatusCode.OK, status)
            val json = parseObject(bodyAsText())
            assertEquals(ACCOUNT_CREATE_SUCCESS, json.getOrDefault("code", -1).toString().toIntOrNull())
        }
    }
}