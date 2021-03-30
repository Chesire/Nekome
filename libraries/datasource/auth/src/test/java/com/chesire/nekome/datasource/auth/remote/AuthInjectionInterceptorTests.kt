package com.chesire.nekome.datasource.auth.remote

import com.chesire.nekome.datasource.auth.AccessTokenRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import okhttp3.Interceptor
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthInjectionInterceptorTests {

    @Test
    fun `authorization header gets added correctly`() {
        val token = "AccessTokenPass"
        val expected = "Bearer $token"
        val mockRepo = mockk<AccessTokenRepository> {
            every { accessToken } returns token
        }
        val headerCapture = slot<String>()
        val mockChain = mockk<Interceptor.Chain> {
            every { request() } returns mockk {
                every { newBuilder() } returns mockk(relaxed = true) {
                    every { header(any(), capture(headerCapture)) } returns this
                }
            }
            every { proceed(any()) } returns mockk()
        }
        val testObject = AuthInjectionInterceptor(mockRepo)

        testObject.intercept(mockChain)

        assertEquals(expected, headerCapture.captured)
    }
}
