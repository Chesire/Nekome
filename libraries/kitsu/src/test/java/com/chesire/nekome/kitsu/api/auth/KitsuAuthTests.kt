package com.chesire.nekome.kitsu.api.auth

import com.chesire.nekome.kitsu.AuthProvider
import com.chesire.nekome.core.Resource
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import java.net.UnknownHostException

class KitsuAuthTests {
    @Test
    fun `login failure response returns Resource#Error with errorBody`() = runBlocking {
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
            coEvery {
                loginAsync(LoginRequest(usernameInput, passwordInput))
            } coAnswers {
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
    fun `login failure response returns Resource#Error with message if no error`() = runBlocking {
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
            coEvery {
                loginAsync(LoginRequest(usernameInput, passwordInput))
            } coAnswers {
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
    fun `login successful response with no body returns Resource#Error`() = runBlocking {
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
            coEvery {
                loginAsync(LoginRequest(usernameInput, passwordInput))
            } coAnswers {
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
    fun `login successful response with body returns Resource#Success`() = runBlocking {
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
            coEvery {
                loginAsync(LoginRequest(usernameInput, passwordInput))
            } coAnswers {
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
    fun `login successful response with body saves an access token`() = runBlocking {
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
            coEvery {
                loginAsync(LoginRequest(usernameInput, passwordInput))
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuAuth(mockService, mockProvider)
        classUnderTest.login(usernameInput, passwordInput)

        assertEquals(expected, slot.captured)
    }

    @Test
    fun `login successful response with body saves a refresh token`() = runBlocking {
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
            coEvery {
                loginAsync(LoginRequest(usernameInput, passwordInput))
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuAuth(mockService, mockProvider)
        classUnderTest.login(usernameInput, passwordInput)

        assertEquals(expected, slot.captured)
    }

    @Test
    fun `login on thrown exception return Resource#Error`() = runBlocking {
        val usernameInput = "username"
        val passwordInput = "password"

        val mockService = mockk<KitsuAuthService> {
            coEvery {
                loginAsync(LoginRequest(usernameInput, passwordInput))
            } throws UnknownHostException()
        }
        val mockProvider = mockk<AuthProvider>()

        val classUnderTest = KitsuAuth(mockService, mockProvider)
        val result = classUnderTest.login(usernameInput, passwordInput)

        when (result) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertTrue(true)
        }
    }

    @Test
    fun `refresh failure response returns Resource#Error with errorBody`() = runBlocking {
        val expected = "errorBodyString"

        val mockProvider = mockk<AuthProvider> {
            every { refreshToken } returns "token"
        }
        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns expected
        }
        val mockResponse = mockk<Response<LoginResponse>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockResponseBody
            every { code() } returns 0
        }
        val mockService = mockk<KitsuAuthService> {
            coEvery {
                refreshAccessTokenAsync(RefreshTokenRequest("token"))
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuAuth(mockService, mockProvider)
        val actual = classUnderTest.refresh()

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `refresh failure response returns Resource#Error with message if no error`() = runBlocking {
        val expected = "responseBodyString"

        val mockProvider = mockk<AuthProvider> {
            every { refreshToken } returns "token"
        }
        val mockResponse = mockk<Response<LoginResponse>> {
            every { isSuccessful } returns false
            every { errorBody() } returns null
            every { message() } returns expected
            every { code() } returns 0
        }
        val mockService = mockk<KitsuAuthService> {
            coEvery {
                refreshAccessTokenAsync(RefreshTokenRequest("token"))
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuAuth(mockService, mockProvider)
        val actual = classUnderTest.refresh()

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `refresh successful response with no body returns Resource#Error`() = runBlocking {
        val expected = "Response body is null"

        val mockProvider = mockk<AuthProvider> {
            every { refreshToken } returns "token"
        }
        val mockResponse = mockk<Response<LoginResponse>> {
            every { isSuccessful } returns true
            every { body() } returns null
            every { message() } returns expected
        }
        val mockService = mockk<KitsuAuthService> {
            coEvery {
                refreshAccessTokenAsync(RefreshTokenRequest("token"))
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuAuth(mockService, mockProvider)
        val actual = classUnderTest.refresh()

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `refresh successful response with body returns Resource#Success`() = runBlocking {
        val loginResponse = LoginResponse("accessToken", 0, 0, "refreshToken", "scope", "tokenType")

        val mockProvider = mockk<AuthProvider> {
            every { accessToken = any() } just Runs
            every { refreshToken } returns "token"
            every { refreshToken = any() } just Runs
        }
        val mockResponse = mockk<Response<LoginResponse>> {
            every { isSuccessful } returns true
            every { body() } returns loginResponse
        }
        val mockService = mockk<KitsuAuthService> {
            coEvery {
                refreshAccessTokenAsync(RefreshTokenRequest("token"))
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuAuth(mockService, mockProvider)
        val actual = classUnderTest.refresh()

        when (actual) {
            is Resource.Success -> assertTrue(true)
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `refresh successful response with body saves an access token`() = runBlocking {
        val expected = "expectedAccessToken"
        val slot = CapturingSlot<String>()
        val loginResponse = LoginResponse(expected, 0, 0, "refreshToken", "scope", "tokenType")

        val mockProvider = mockk<AuthProvider> {
            every { accessToken = capture(slot) } just Runs
            every { refreshToken } returns "token"
            every { refreshToken = any() } just Runs
        }
        val mockResponse = mockk<Response<LoginResponse>> {
            every { isSuccessful } returns true
            every { body() } returns loginResponse
        }
        val mockService = mockk<KitsuAuthService> {
            coEvery {
                refreshAccessTokenAsync(RefreshTokenRequest("token"))
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuAuth(mockService, mockProvider)
        classUnderTest.refresh()

        assertEquals(expected, slot.captured)
    }

    @Test
    fun `refresh successful response with body saves a refresh token`() = runBlocking {
        val expected = "expectedRefreshToken"
        val slot = CapturingSlot<String>()
        val loginResponse = LoginResponse("accessToken", 0, 0, expected, "scope", "tokenType")

        val mockProvider = mockk<AuthProvider> {
            every { accessToken = any() } just Runs
            every { refreshToken } returns "token"
            every { refreshToken = capture(slot) } just Runs
        }
        val mockResponse = mockk<Response<LoginResponse>> {
            every { isSuccessful } returns true
            every { body() } returns loginResponse
        }
        val mockService = mockk<KitsuAuthService> {
            coEvery {
                refreshAccessTokenAsync(RefreshTokenRequest("token"))
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuAuth(mockService, mockProvider)
        classUnderTest.refresh()

        assertEquals(expected, slot.captured)
    }

    @Test
    fun `refresh on thrown exception return Resource#Error`() = runBlocking {
        val tokenInput = "token"

        val mockService = mockk<KitsuAuthService> {
            coEvery {
                refreshAccessTokenAsync(RefreshTokenRequest(tokenInput))
            } throws UnknownHostException()
        }
        val mockProvider = mockk<AuthProvider> {
            every { refreshToken } returns tokenInput
        }

        val classUnderTest = KitsuAuth(mockService, mockProvider)
        val result = classUnderTest.refresh()

        when (result) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertTrue(true)
        }
    }

    @Test
    fun `clearAuth clears auth in the provider`() = runBlocking {
        val mockService = mockk<KitsuAuthService>()
        val mockProvider = mockk<AuthProvider> {
            every { clearAuth() } just Runs
        }

        val classUnderTest = KitsuAuth(mockService, mockProvider)
        classUnderTest.clearAuth()

        verify { mockProvider.clearAuth() }
    }
}
