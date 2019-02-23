package com.chesire.malime.kitsu.interceptors

import com.chesire.malime.kitsu.AuthProvider
import com.chesire.malime.kitsu.api.auth.KitsuAuthService
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class AuthRefreshInterceptorTests {
    private val mockWebServer = MockWebServer()

    @Before
    fun setup() = mockWebServer.start()

    @After
    fun teardown() = mockWebServer.shutdown()

    @Test
    @Ignore("Come back this test later")
    fun `successful response just returns response`() {

    }

    @Test
    @Ignore("Come back this test later")
    fun `failure response with code !403 just returns response`() {

    }

    @Test
    @Ignore("Come back this test later")
    fun `getting new auth failure, returns error response with 401`() = runBlocking {

    }

    @Test
    @Ignore("Come back this test later")
    fun `getting new auth success with no body, returns error response with 401`() = runBlocking {

    }

    @Test
    @Ignore("Come back this test later")
    fun `getting new auth stores new accessToken`() = runBlocking {

    }

    @Test
    @Ignore("Come back this test later")
    fun `getting new auth stores new refreshToken`() = runBlocking {

    }

    @Test
    @Ignore("Come back this test later")
    fun `getting new auth adds new auth to header of next request`() = runBlocking {

    }

    @Test
    @Ignore("Come back this test later")
    fun `getting new auth retries previous request`() = runBlocking {

    }
}
