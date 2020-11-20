package com.chesire.nekome.kitsu.interceptors

import com.chesire.nekome.auth.api.AuthApi
import com.chesire.nekome.core.Resource
import com.chesire.nekome.kitsu.AuthProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthRefreshInterceptorTests {
    @Test
    fun `successful response just returns response`() = runBlocking {
        val mockProvider = mockk<AuthProvider>()
        val mockAuth = mockk<AuthApi>()
        val response = mockk<Response> {
            every { isSuccessful } returns true
        }
        val mockChain = mockk<Interceptor.Chain> {
            every { request() } returns mockk()
            every { proceed(any()) } returns response
        }
        val testObject = AuthRefreshInterceptor(mockProvider, mockAuth)

        val result = testObject.intercept(mockChain)

        coVerify(exactly = 0) { mockAuth.refresh() }
        assertTrue(result.isSuccessful)
    }

    @Test
    fun `failure response with code !403 just returns response`() = runBlocking {
        val mockProvider = mockk<AuthProvider>()
        val mockAuth = mockk<AuthApi>()
        val response = mockk<Response> {
            every { isSuccessful } returns false
            every { code() } returns 404
        }
        val mockChain = mockk<Interceptor.Chain> {
            every { request() } returns mockk()
            every { proceed(any()) } returns response
        }
        val testObject = AuthRefreshInterceptor(mockProvider, mockAuth)

        val result = testObject.intercept(mockChain)

        coVerify(exactly = 0) { mockAuth.refresh() }
        assertEquals(response, result)
    }

    @Test
    fun `getting new auth failure, returns error response with 401`() = runBlocking {
        val mockProvider = mockk<AuthProvider>()
        val mockAuth = mockk<AuthApi> {
            coEvery {
                refresh()
            } coAnswers {
                Resource.Error("Failure")
            }
        }
        val response = mockk<Response> {
            every { isSuccessful } returns false
            every { code() } returns 403
            every { request() } returns mockk()
            every { protocol() } returns mockk()
            every { message() } returns "message"
        }
        val mockChain = mockk<Interceptor.Chain> {
            every { request() } returns mockk()
            every { proceed(any()) } returns response
        }
        val testObject = AuthRefreshInterceptor(mockProvider, mockAuth)

        val result = testObject.intercept(mockChain)

        coVerify(exactly = 1) { mockAuth.refresh() }
        assertEquals(401, result.code())
    }

    @Test
    fun `getting new auth retries previous request`() = runBlocking {
        val mockProvider = mockk<AuthProvider> {
            every { accessToken } returns "accessToken"
        }
        val mockAuth = mockk<AuthApi> {
            coEvery {
                refresh()
            } coAnswers {
                Resource.Success(mockk())
            }
        }
        val response = mockk<Response> {
            every { isSuccessful } returns false
            every { code() } returns 403
            every { request() } returns mockk()
            every { protocol() } returns mockk()
            every { message() } returns "message"
        }
        val mockChain = mockk<Interceptor.Chain> {
            every { request() } returns mockk(relaxed = true)
            every { proceed(any()) } returns response
        }
        val testObject = AuthRefreshInterceptor(mockProvider, mockAuth)

        testObject.intercept(mockChain)

        verify(exactly = 2) { mockChain.proceed(any()) }
    }
}
