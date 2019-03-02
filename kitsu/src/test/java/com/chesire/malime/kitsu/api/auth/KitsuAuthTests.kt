package com.chesire.malime.kitsu.api.auth

import com.chesire.malime.core.Resource
import com.chesire.malime.kitsu.AuthProvider
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class KitsuAuthTests {
    @Test
    fun `failure response returns Resource#Error with errorBody`() = runBlocking {
        val usernameInput = "username"
        val passwordInput = "password"
        val expected = "errorBodyString"

        val mockProvider = mockk<AuthProvider>()
        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns expected
        }
        val mockResponse = mockk<Response<LoginResponse>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockResponseBody
            every { code() } returns 0
        }
        val mockService = mockk<KitsuAuthService> {
            every {
                loginAsync(LoginRequest(usernameInput, passwordInput))
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuAuth(mockService, mockProvider)
        val actual = classUnderTest.login(usernameInput, passwordInput)

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `failure response returns Resource#Error with message if no error`() = runBlocking {
        val usernameInput = "username"
        val passwordInput = "password"
        val expected = "responseBodyString"

        val mockProvider = mockk<AuthProvider>()
        val mockResponse = mockk<Response<LoginResponse>> {
            every { isSuccessful } returns false
            every { errorBody() } returns null
            every { message() } returns expected
            every { code() } returns 0
        }
        val mockService = mockk<KitsuAuthService> {
            every {
                loginAsync(LoginRequest(usernameInput, passwordInput))
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuAuth(mockService, mockProvider)
        val actual = classUnderTest.login(usernameInput, passwordInput)

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `successful response with no body returns Resource#Error`() = runBlocking {
        val usernameInput = "username"
        val passwordInput = "password"
        val expected = "Response body is null"

        val mockProvider = mockk<AuthProvider>()
        val mockResponse = mockk<Response<LoginResponse>> {
            every { isSuccessful } returns true
            every { body() } returns null
            every { message() } returns expected
        }
        val mockService = mockk<KitsuAuthService> {
            every {
                loginAsync(LoginRequest(usernameInput, passwordInput))
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuAuth(mockService, mockProvider)
        val actual = classUnderTest.login(usernameInput, passwordInput)

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `successful response with body returns Resource#Success`() = runBlocking {
        val usernameInput = "username"
        val passwordInput = "password"
        val loginResponse = LoginResponse("accessToken", 0, 0, "refreshToken", "scope", "tokenType")

        val mockProvider = mockk<AuthProvider> {
            every { accessToken = any() } just Runs
            every { refreshToken = any() } just Runs
        }
        val mockResponse = mockk<Response<LoginResponse>> {
            every { isSuccessful } returns true
            every { body() } returns loginResponse
        }
        val mockService = mockk<KitsuAuthService> {
            every {
                loginAsync(LoginRequest(usernameInput, passwordInput))
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuAuth(mockService, mockProvider)
        val actual = classUnderTest.login(usernameInput, passwordInput)

        when (actual) {
            is Resource.Success -> assertTrue(true)
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `successful response with body saves an access token`() = runBlocking {
        val usernameInput = "username"
        val passwordInput = "password"
        val expected = "expectedAccessToken"
        val slot = CapturingSlot<String>()
        val loginResponse = LoginResponse(expected, 0, 0, "refreshToken", "scope", "tokenType")

        val mockProvider = mockk<AuthProvider> {
            every { accessToken = capture(slot) } just Runs
            every { refreshToken = any() } just Runs
        }
        val mockResponse = mockk<Response<LoginResponse>> {
            every { isSuccessful } returns true
            every { body() } returns loginResponse
        }
        val mockService = mockk<KitsuAuthService> {
            every {
                loginAsync(LoginRequest(usernameInput, passwordInput))
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuAuth(mockService, mockProvider)
        classUnderTest.login(usernameInput, passwordInput)

        assertEquals(expected, slot.captured)
    }

    @Test
    fun `successful response with body saves a refresh token`() = runBlocking {
        val usernameInput = "username"
        val passwordInput = "password"
        val expected = "expectedRefreshToken"
        val slot = CapturingSlot<String>()
        val loginResponse = LoginResponse("accessToken", 0, 0, expected, "scope", "tokenType")

        val mockProvider = mockk<AuthProvider> {
            every { accessToken = any() } just Runs
            every { refreshToken = capture(slot) } just Runs
        }
        val mockResponse = mockk<Response<LoginResponse>> {
            every { isSuccessful } returns true
            every { body() } returns loginResponse
        }
        val mockService = mockk<KitsuAuthService> {
            every {
                loginAsync(LoginRequest(usernameInput, passwordInput))
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuAuth(mockService, mockProvider)
        classUnderTest.login(usernameInput, passwordInput)

        assertEquals(expected, slot.captured)
    }
}
