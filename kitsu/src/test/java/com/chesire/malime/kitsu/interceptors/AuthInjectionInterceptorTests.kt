package com.chesire.malime.kitsu.interceptors

import com.chesire.malime.kitsu.AuthProvider
import io.mockk.every
import io.mockk.mockk
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AuthInjectionInterceptorTests {
    private val mockWebServer = MockWebServer()

    @Before
    fun setup() = mockWebServer.start()

    @After
    fun teardown() = mockWebServer.shutdown()

    @Test
    fun `authorization header gets added correctly`() {
        val token = "AccessToken"
        val expected = "Bearer $token"
        val mockProvider = mockk<AuthProvider> {
            every { accessToken } returns token
        }
        val client = OkHttpClient()
            .newBuilder()
            .addInterceptor(AuthInjectionInterceptor(mockProvider))
            .build()

        mockWebServer.enqueue(MockResponse())

        client.newCall(
            Request
                .Builder()
                .url(mockWebServer.url("/"))
                .build()
        ).execute()
        val request = mockWebServer.takeRequest()

        assertEquals(expected, request.headers.get("Authorization"))
    }
}
