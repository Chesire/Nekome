package com.chesire.malime.kitsu

import com.chesire.malime.core.Resource
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import java.net.UnknownHostException

class ResponseParsingTests {
    @Test
    fun `Response#parse !isSuccessful false returns Resource#Error`() {
        val response = Response.error<Any>(400, mockk {
            every { string() } returns ""
        })
        assertTrue(response.parse() is Resource.Error)
    }

    @Test
    fun `Response#parse isSuccessful null body returns Resource#Error`() {
        val response = Response.success<Any>(null)
        assertTrue(response.parse() is Resource.Error)
    }

    @Test
    fun `Response#parse isSuccessful with body returns Resource#Success`() {
        val response = Response.success<Any>(mockk())
        assertTrue(response.parse() is Resource.Success)
    }

    @Test
    fun `parseError with errorBody returns error`() {
        val expected = "expectedError"
        val response = Response.error<Any>(400, mockk {
            every { string() } returns expected
        })
        assertEquals(expected, response.parseError().msg)
    }

    @Test
    fun `parseError contains the code`() {
        val expected = "expectedError"
        val response = Response.error<Any>(400, mockk {
            every { string() } returns expected
        })
        assertEquals(400, response.parseError().code)
    }

    @Test
    fun `emptyResponseError contains msg`() {
        val error = Resource.Error<Any>("Response body is null", 204)
        assertEquals("Response body is null", error.msg)
    }

    @Test
    fun `emptyResponseError code is 204`() {
        val error = Resource.Error<Any>("Response body is null", 204)
        assertEquals(204, error.code)
    }

    @Test
    fun `Exception#parse handles UnknownHostException`() {
        val exception = UnknownHostException().parse<Any>()
        assertEquals("Could not reach service", exception.msg)
        assertEquals(503, exception.code)
    }

    @Test
    fun `Exception#parse handles generic exception`() {
        // use some random exception to simulate
        val exception = NullPointerException().parse<Any>()
        assertEquals("Unknown error encountered", exception.msg)
        assertEquals(400, exception.code)
    }
}
