package com.chesire.nekome.datasource.auth.remote

import com.chesire.nekome.core.AuthCaster
import com.chesire.nekome.datasource.auth.AccessTokenRepository
import com.chesire.nekome.datasource.auth.AccessTokenResult
import com.chesire.nekome.datasource.auth.AuthException
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

@Suppress("SwallowedException")
class AuthRefreshInterceptorTests {

    @Test
    fun `successful response just returns response`() = runBlocking {
        val mockRepo = mockk<AccessTokenRepository>()
        val mockAuthCaster = mockk<AuthCaster>()
        val response = mockk<Response> {
            every { isSuccessful } returns true
        }
        val mockChain = mockk<Interceptor.Chain> {
            every { request() } returns mockk()
            every { proceed(any()) } returns response
        }
        val testObject = AuthRefreshInterceptor(mockRepo, mockAuthCaster)

        val result = testObject.intercept(mockChain)

        coVerify(exactly = 0) { mockRepo.refresh() }
        assertTrue(result.isSuccessful)
    }

    @Test
    fun `failure response with code !403 just returns response`() = runBlocking {
        val mockRepo = mockk<AccessTokenRepository>()
        val mockAuthCaster = mockk<AuthCaster>()
        val response = mockk<Response> {
            every { isSuccessful } returns false
            every { code() } returns 404
        }
        val mockChain = mockk<Interceptor.Chain> {
            every { request() } returns mockk()
            every { proceed(any()) } returns response
        }
        val testObject = AuthRefreshInterceptor(mockRepo, mockAuthCaster)

        val result = testObject.intercept(mockChain)

        coVerify(exactly = 0) { mockRepo.refresh() }
        assertEquals(response, result)
    }

    @Test
    fun `getting new auth failure, notifies authCaster issue refreshing`() = runBlocking {
        val mockRepo = mockk<AccessTokenRepository> {
            coEvery { refresh() } returns AccessTokenResult.CommunicationError
        }
        val mockAuthCaster = mockk<AuthCaster> {
            every { issueRefreshingToken() } just Runs
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
        val testObject = AuthRefreshInterceptor(mockRepo, mockAuthCaster)

        try {
            testObject.intercept(mockChain)
        } catch (ex: AuthException) {
            // Ignore the crash
        }

        coVerify(exactly = 1) { mockRepo.refresh() }
        verify { mockAuthCaster.issueRefreshingToken() }
    }

    @Test(expected = AuthException::class)
    fun `getting new auth failure, throws AuthException`() = runBlocking {
        val mockRepo = mockk<AccessTokenRepository> {
            coEvery { refresh() } returns AccessTokenResult.CommunicationError
        }
        val mockAuthCaster = mockk<AuthCaster> {
            every { issueRefreshingToken() } just Runs
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
        val testObject = AuthRefreshInterceptor(mockRepo, mockAuthCaster)

        testObject.intercept(mockChain)

        fail("Exception not thrown")
    }

    @Test
    fun `getting new auth retries previous request`() = runBlocking {
        val mockRepo = mockk<AccessTokenRepository> {
            every { accessToken } returns "accessToken"
            coEvery { refresh() } returns AccessTokenResult.Success
        }
        val mockAuthCaster = mockk<AuthCaster>()
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
        val testObject = AuthRefreshInterceptor(mockRepo, mockAuthCaster)

        testObject.intercept(mockChain)

        verify(exactly = 2) { mockChain.proceed(any()) }
    }
}
