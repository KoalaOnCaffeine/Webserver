package me.tomnewton.plugins

import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import me.tomnewton.Messages
import kotlin.test.Test
import kotlin.test.assertEquals

class RoutingKtTest {

    /**
     * Handle a request to a test server at the given [endpoint]. The default method, give in the [builder] is [Get][HttpMethod.Get]
     * The test application is not cached
     * @param endpoint The endpoint to test
     * @param builder The builder method for the request which by default sets the method to [Get][HttpMethod.Get]
     * @param block The testing method
     */

    private fun testHandle(
        endpoint: String,
        builder: HttpRequestBuilder.() -> Unit = { method = HttpMethod.Get },
        block: suspend HttpResponse.() -> Unit
    ) {
        testApplication {
            application {
                configureRouting()
                configureStatusPages()
            }
            client.request {
                builder()
                url(endpoint)
            } // Create a request using the builder, but make sure the endpoint cannot be changed
                .run { block() } // Run the test
        }
    }

    @Test
    fun `Verify serving Hello World page`() {
        testHandle("/") {
            // Response code should be the OK code, 200
            assertEquals(HttpStatusCode.OK, status)
            // Content of the page should be "Hello world!"
            assertEquals("Hello World!", body()) // body returns the raw content, e.g. unparsed html
        }
    }

    @Test
    fun `default endpoint`() {
        // Client request exception should be thrown
        try {
            testHandle("/something") {}
            assert(false) // Make sure to fail the test if it doesn't throw an error
        } catch (exception: ClientRequestException) {
            with(exception) {
                // Response code should be the Not Found code, 404
                assertEquals(HttpStatusCode.NotFound, response.status)
                // Content of the page should be the notFoundMessage
                runBlocking { assertEquals(Messages.notFoundMessage, response.body()) } // Avoid suspend fun
            }
        }
    }
}