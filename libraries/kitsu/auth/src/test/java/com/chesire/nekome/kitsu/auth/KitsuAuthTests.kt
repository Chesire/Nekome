package com.chesire.nekome.kitsu.auth

import com.chesire.nekome.datasource.auth.AuthException
import com.chesire.nekome.datasource.auth.remote.AuthResult
import com.chesire.nekome.kitsu.auth.dto.AuthResponseDto
import com.chesire.nekome.kitsu.auth.dto.LoginRequestDto
import com.chesire.nekome.kitsu.auth.dto.RefreshTokenRequestDto
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response
import java.net.UnknownHostException

private const val USERNAME_INPUT = "username"
private const val PASSWORD_INPUT = "password"
private const val REFRESH_TOKEN_INPUT = "refreshToken"

class KitsuAuthTests {

    @Test
    fun `login failure response returns AuthResult#BadRequest`() = runBlocking {
        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns "errorBodyString"
        }
        val mockResponse = mockk<Response<AuthResponseDto>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockResponseBody
            every { code() } returns 0
        }
        val mockService = mockk<KitsuAuthService> {
            coEvery {
                loginAsync(LoginRequestDto(USERNAME_INPUT, PASSWORD_INPUT))
            } returns mockResponse
        }
        val classUnderTest = KitsuAuth(mockService)

        val actual = classUnderTest.login(USERNAME_INPUT, PASSWORD_INPUT)

        assertEquals(AuthResult.BadRequest, actual)
    }

    @Test
    fun `login successful response with no body returns AuthResult#BadRequest`() = runBlocking {
        val mockResponse = mockk<Response<AuthResponseDto>> {
            every { isSuccessful } returns true
            every { body() } returns null
            every { message() } returns "Response body is null"
        }
        val mockService = mockk<KitsuAuthService> {
            coEvery {
                loginAsync(LoginRequestDto(USERNAME_INPUT, PASSWORD_INPUT))
            } returns mockResponse
        }
        val classUnderTest = KitsuAuth(mockService)

        val actual = classUnderTest.login(USERNAME_INPUT, PASSWORD_INPUT)

        assertEquals(AuthResult.BadRequest, actual)
    }

    @Test
    fun `login successful response with body returns AuthResult#Success`() = runBlocking {
        val expectedAccessToken = "accessToken"
        val expectedRefreshToken = "refreshToken"
        val loginResponse = AuthResponseDto(expectedAccessToken, expectedRefreshToken)
        val mockResponse = mockk<Response<AuthResponseDto>> {
            every { isSuccessful } returns true
            every { body() } returns loginResponse
        }
        val mockService = mockk<KitsuAuthService> {
            coEvery {
                loginAsync(LoginRequestDto(USERNAME_INPUT, PASSWORD_INPUT))
            } returns mockResponse
        }
        val classUnderTest = KitsuAuth(mockService)

        val actual = classUnderTest.login(USERNAME_INPUT, PASSWORD_INPUT)

        check(actual is AuthResult.Success)
        assertEquals(expectedAccessToken, actual.accessToken)
        assertEquals(expectedRefreshToken, actual.refreshToken)
    }

    @Test
    fun `login on thrown UnknownHostException return AuthResult#CouldNotReachServer`() =
        runBlocking {
            val mockService = mockk<KitsuAuthService> {
                coEvery {
                    loginAsync(LoginRequestDto(USERNAME_INPUT, PASSWORD_INPUT))
                } throws UnknownHostException()
            }
            val classUnderTest = KitsuAuth(mockService)

            val result = classUnderTest.login(USERNAME_INPUT, PASSWORD_INPUT)

            assertEquals(AuthResult.CouldNotReachServer, result)
        }

    @Test
    fun `login on thrown AuthException return AuthResult#CouldNotRefresh`() = runBlocking {
        val mockService = mockk<KitsuAuthService> {
            coEvery {
                loginAsync(LoginRequestDto(USERNAME_INPUT, PASSWORD_INPUT))
            } throws AuthException()
        }
        val classUnderTest = KitsuAuth(mockService)

        val result = classUnderTest.login(USERNAME_INPUT, PASSWORD_INPUT)

        assertEquals(AuthResult.CouldNotRefresh, result)
    }

    @Test
    fun `login on thrown other Exception return AuthResult#BadRequest`() = runBlocking {
        val mockService = mockk<KitsuAuthService> {
            coEvery {
                loginAsync(LoginRequestDto(USERNAME_INPUT, PASSWORD_INPUT))
            } throws IllegalArgumentException()
        }
        val classUnderTest = KitsuAuth(mockService)

        val result = classUnderTest.login(USERNAME_INPUT, PASSWORD_INPUT)

        assertEquals(AuthResult.BadRequest, result)
    }

    @Test
    fun `refresh failure response returns AuthResult#BadRequest`() = runBlocking {
        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns "errorBodyString"
        }
        val mockResponse = mockk<Response<AuthResponseDto>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockResponseBody
            every { code() } returns 0
        }
        val mockService = mockk<KitsuAuthService> {
            coEvery {
                refreshAccessTokenAsync(RefreshTokenRequestDto("token"))
            } returns mockResponse
        }
        val classUnderTest = KitsuAuth(mockService)

        val actual = classUnderTest.refresh(REFRESH_TOKEN_INPUT)

        assertEquals(AuthResult.BadRequest, actual)
    }

    @Test
    fun `refresh failure response returns AuthResult#BadRequest with message if no error`() =
        runBlocking {
            val mockResponse = mockk<Response<AuthResponseDto>> {
                every { isSuccessful } returns false
                every { errorBody() } returns null
                every { message() } returns "responseBodyString"
                every { code() } returns 0
            }
            val mockService = mockk<KitsuAuthService> {
                coEvery {
                    refreshAccessTokenAsync(RefreshTokenRequestDto("token"))
                } returns mockResponse
            }
            val classUnderTest = KitsuAuth(mockService)

            val actual = classUnderTest.refresh(REFRESH_TOKEN_INPUT)

            assertEquals(AuthResult.BadRequest, actual)
        }

    @Test
    fun `refresh successful response with no body returns AuthResult#BadRequest`() = runBlocking {
        val mockResponse = mockk<Response<AuthResponseDto>> {
            every { isSuccessful } returns true
            every { body() } returns null
            every { message() } returns "Response body is null"
        }
        val mockService = mockk<KitsuAuthService> {
            coEvery {
                refreshAccessTokenAsync(RefreshTokenRequestDto("token"))
            } returns mockResponse
        }
        val classUnderTest = KitsuAuth(mockService)

        val actual = classUnderTest.refresh(REFRESH_TOKEN_INPUT)

        assertEquals(AuthResult.BadRequest, actual)
    }

    @Test
    fun `refresh successful response with body returns AuthResult#Success`() = runBlocking {
        val expectedAccessToken = "accessToken"
        val expectedRefreshToken = "refreshToken"
        val loginResponse = AuthResponseDto(expectedAccessToken, expectedRefreshToken)
        val mockResponse = mockk<Response<AuthResponseDto>> {
            every { isSuccessful } returns true
            every { body() } returns loginResponse
        }
        val mockService = mockk<KitsuAuthService> {
            coEvery {
                refreshAccessTokenAsync(RefreshTokenRequestDto(REFRESH_TOKEN_INPUT))
            } returns mockResponse
        }
        val classUnderTest = KitsuAuth(mockService)

        val actual = classUnderTest.refresh(REFRESH_TOKEN_INPUT)

        check(actual is AuthResult.Success)
        assertEquals(expectedAccessToken, actual.accessToken)
        assertEquals(expectedRefreshToken, actual.refreshToken)
    }

    @Test
    fun `refresh on thrown UnknownHostException return AuthResult#CouldNotReachServer`() =
        runBlocking {
            val mockService = mockk<KitsuAuthService> {
                coEvery {
                    refreshAccessTokenAsync(RefreshTokenRequestDto(REFRESH_TOKEN_INPUT))
                } throws UnknownHostException()
            }
            val classUnderTest = KitsuAuth(mockService)

            val actual = classUnderTest.refresh(REFRESH_TOKEN_INPUT)

            assertEquals(AuthResult.CouldNotReachServer, actual)
        }

    @Test
    fun `refresh on thrown AuthException return AuthResult#CouldNotRefresh`() = runBlocking {
        val mockService = mockk<KitsuAuthService> {
            coEvery {
                refreshAccessTokenAsync(RefreshTokenRequestDto(REFRESH_TOKEN_INPUT))
            } throws AuthException()
        }
        val classUnderTest = KitsuAuth(mockService)

        val actual = classUnderTest.refresh(REFRESH_TOKEN_INPUT)

        assertEquals(AuthResult.CouldNotRefresh, actual)
    }

    @Test
    fun `refresh on thrown other exception return AuthResult#BadRequest`() = runBlocking {
        val mockService = mockk<KitsuAuthService> {
            coEvery {
                refreshAccessTokenAsync(RefreshTokenRequestDto(REFRESH_TOKEN_INPUT))
            } throws IllegalArgumentException()
        }
        val classUnderTest = KitsuAuth(mockService)

        val actual = classUnderTest.refresh(REFRESH_TOKEN_INPUT)

        assertEquals(AuthResult.BadRequest, actual)
    }
}
