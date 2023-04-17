package com.chesire.nekome.kitsu

import io.mockk.every
import io.mockk.mockk
import java.net.UnknownHostException
import okhttp3.MediaType
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class ResponseParsingTests {

    @Test
    fun `asError with errorBody returns error`() {
        val expected = "expectedError"
        val response = Response.error<Any>(
            400,
            mockk {
                every { string() } returns expected
                every { contentType() } returns MediaType.get("application/json")
                every { contentLength() } returns 0
            }
        )
        assertEquals(expected, response.asError().message)
    }

    @Test
    fun `asError contains the code`() {
        val expected = "expectedError"
        val response = Response.error<Any>(
            400,
            mockk {
                every { string() } returns expected
                every { contentType() } returns MediaType.get("application/json")
                every { contentLength() } returns 0
            }
        )
        assertEquals(400, response.asError().code)
    }

    @Test
    fun `Exception#parse handles UnknownHostException`() {
        val exception = UnknownHostException().parse()
        assertEquals("Could not reach service", exception.message)
        assertEquals(503, exception.code)
    }

    @Test
    fun `Exception#parse handles generic exception`() {
        // use some random exception to simulate
        val exception = NullPointerException().parse()
        assertEquals("java.lang.NullPointerException", exception.message)
        assertEquals(400, exception.code)
    }
}
