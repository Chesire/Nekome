package com.chesire.nekome.datasource.auth.remote

import com.chesire.nekome.core.AuthCaster
import com.chesire.nekome.datasource.auth.AccessTokenRepository
import com.chesire.nekome.datasource.auth.AccessTokenResult
import com.chesire.nekome.datasource.auth.AuthException
import io.mockk.Runs
import io.mockk.clearAllMocks
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
import org.junit.Before
import org.junit.Test

@Suppress("SwallowedException")
class AuthRefreshInterceptorTests {

    private val mockRepo = mockk<AccessTokenRepository>()
    private val mockAuthCaster = mockk<AuthCaster>()
    private lateinit var testObject: AuthRefreshInterceptor

    @Before
    fun setup() {
        clearAllMocks()

        testObject = AuthRefreshInterceptor(mockRepo, mockAuthCaster)
    }

    @Test
    fun `successful response just returns response`() = runBlocking {
        val response = mockk<Response> {
            every { isSuccessful } returns true
            every { code() } returns 200
        }
        val mockChain = mockk<Interceptor.Chain> {
            every { request() } returns mockk()
            every { proceed(any()) } returns response
        }

        val result = testObject.intercept(mockChain)

        coVerify(exactly = 0) { mockRepo.refresh() }
        assertTrue(result.isSuccessful)
    }

    @Test
    fun `failure response with code !403 just returns response`() = runBlocking {
        val response = mockk<Response> {
            every { isSuccessful } returns false
            every { code() } returns 404
        }
        val mockChain = mockk<Interceptor.Chain> {
            every { request() } returns mockk()
            every { proceed(any()) } returns response
        }

        val result = testObject.intercept(mockChain)

        coVerify(exactly = 0) { mockRepo.refresh() }
        assertEquals(response, result)
    }

    @Test
    fun `getting 401 failure, attempts to refresh token`() = runBlocking {
        coEvery { mockRepo.refresh() } returns AccessTokenResult.CommunicationError
        every { mockAuthCaster.issueRefreshingToken() } just Runs
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

        try {
            testObject.intercept(mockChain)
        } catch (ex: AuthException) {
            // Ignore the crash
        }

        coVerify(exactly = 1) { mockRepo.refresh() }
    }

    @Test
    fun `getting 403 failure, attempts to refresh token`() = runBlocking {
        coEvery { mockRepo.refresh() } returns AccessTokenResult.CommunicationError
        every { mockAuthCaster.issueRefreshingToken() } just Runs
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

        try {
            testObject.intercept(mockChain)
        } catch (ex: AuthException) {
            // Ignore the crash
        }

        coVerify(exactly = 1) { mockRepo.refresh() }
    }

    @Test
    fun `getting new auth failure, notifies authCaster issue refreshing`() = runBlocking {
        coEvery { mockRepo.refresh() } returns AccessTokenResult.CommunicationError
        every { mockAuthCaster.issueRefreshingToken() } just Runs
        val response = mockk<Response> {
            every { isSuccessful } returns false
            every { code() } returns 401
            every { request() } returns mockk()
            every { protocol() } returns mockk()
            every { message() } returns "message"
        }
        val mockChain = mockk<Interceptor.Chain> {
            every { request() } returns mockk()
            every { proceed(any()) } returns response
        }

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
        coEvery { mockRepo.refresh() } returns AccessTokenResult.CommunicationError
        every { mockAuthCaster.issueRefreshingToken() } just Runs
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

        testObject.intercept(mockChain)

        fail("Exception not thrown")
    }

    @Test
    fun `getting new auth retries previous request`() = runBlocking {
        mockRepo.apply {
            every { accessToken } returns "accessToken"
            coEvery { refresh() } returns AccessTokenResult.Success
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

        testObject.intercept(mockChain)

        verify(exactly = 2) { mockChain.proceed(any()) }
    }
}
