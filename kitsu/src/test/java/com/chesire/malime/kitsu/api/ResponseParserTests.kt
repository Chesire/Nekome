package com.chesire.malime.kitsu.api

import com.chesire.malime.core.Resource
import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class ResponseParserTests {
    @Test
    fun `failure response returns Resource#Error with errorBody`() {
        val expected = "errorBodyString"
        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns expected
        }
        val mockResponse = mockk<Response<Any>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockResponseBody
            every { code() } returns 0
        }

        val responseParser = TestResponseParser()
        val actual = responseParser.parseResponse(mockResponse) as Resource.Error

        assertEquals(expected, actual.msg)
    }

    @Test
    fun `failure response returns Resource#Error with response message if no error`() {
        val expected = "responseBodyString"
        val mockResponse = mockk<Response<Any>> {
            every { isSuccessful } returns false
            every { errorBody() } returns null
            every { message() } returns expected
            every { code() } returns 0
        }

        val responseParser = TestResponseParser()
        val actual = responseParser.parseResponse(mockResponse) as Resource.Error

        assertEquals(expected, actual.msg)
    }

    @Test
    fun `successful response with no body returns Resource#Error`() {
        val expected = "Response body is null"
        val mockResponse = mockk<Response<Any>> {
            every { isSuccessful } returns true
            every { body() } returns null
            every { message() } returns expected
        }

        val responseParser = TestResponseParser()
        val actual = responseParser.parseResponse(mockResponse) as Resource.Error

        assertEquals(expected, actual.msg)
    }

    @Test
    fun `successful response with body returns Resource#Success`() {
        val expected = "Expected result"
        val mockResponse = mockk<Response<Any>> {
            every { isSuccessful } returns true
            every { body() } returns expected
        }

        val responseParser = TestResponseParser()
        val actual = responseParser.parseResponse(mockResponse) as Resource.Success

        assertEquals(expected, actual.data)
    }

    private inner class TestResponseParser : ResponseParser
}
