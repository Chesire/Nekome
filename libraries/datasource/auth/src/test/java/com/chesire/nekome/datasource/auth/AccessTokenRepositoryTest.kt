package com.chesire.nekome.datasource.auth

import com.chesire.nekome.datasource.auth.local.AuthProvider
import com.chesire.nekome.datasource.auth.remote.AuthApi
import com.chesire.nekome.datasource.auth.remote.AuthDomain
import com.chesire.nekome.datasource.auth.remote.AuthFailure
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private const val USERNAME_INPUT = "username"
private const val PASSWORD_INPUT = "password"
private const val REFRESH_TOKEN_INPUT = "oldAccessToken"
private const val ACCESS_TOKEN = "accessToken"
private const val REFRESH_TOKEN = "refreshToken"

class AccessTokenRepositoryTest {

    private lateinit var authProvider: AuthProvider
    private lateinit var authApi: AuthApi
    private lateinit var accessTokenRepository: AccessTokenRepository

    @Before
    fun setup() {
        authProvider = mockk(relaxed = true) {
            every { accessToken } returns ACCESS_TOKEN
            every { refreshToken } returns REFRESH_TOKEN_INPUT
        }
        authApi = mockk()

        accessTokenRepository = AccessTokenRepository(authProvider, authApi)
    }

    @Test
    fun `accessToken returns from the provider`() {
        val actual = accessTokenRepository.accessToken

        assertEquals(ACCESS_TOKEN, actual)
    }

    @Test
    fun `login on success, stores the accessToken`() = runBlocking {
        coEvery {
            authApi.login(USERNAME_INPUT, PASSWORD_INPUT)
        } returns Ok(AuthDomain(ACCESS_TOKEN, REFRESH_TOKEN))

        accessTokenRepository.login(USERNAME_INPUT, PASSWORD_INPUT)

        verify { authProvider.accessToken = ACCESS_TOKEN }
    }

    @Test
    fun `login on success, stores the refreshToken`() = runBlocking {
        coEvery {
            authApi.login(USERNAME_INPUT, PASSWORD_INPUT)
        } returns Ok(AuthDomain(ACCESS_TOKEN, REFRESH_TOKEN))

        accessTokenRepository.login(USERNAME_INPUT, PASSWORD_INPUT)

        verify { authProvider.refreshToken = REFRESH_TOKEN }
    }

    @Test
    fun `login on success, returns AccessTokenResult#Success`() = runBlocking {
        coEvery {
            authApi.login(USERNAME_INPUT, PASSWORD_INPUT)
        } returns Ok(AuthDomain(ACCESS_TOKEN, REFRESH_TOKEN))

        val result = accessTokenRepository.login(USERNAME_INPUT, PASSWORD_INPUT)

        assertTrue(result is AccessTokenResult.Success)
    }

    @Test
    fun `login failure with InvalidCredentials, returns AccessTokenResult#InvalidCredentials`() =
        runBlocking {
            coEvery {
                authApi.login(USERNAME_INPUT, PASSWORD_INPUT)
            } returns Err(AuthFailure.InvalidCredentials)

            val actual = accessTokenRepository.login(USERNAME_INPUT, PASSWORD_INPUT)

            assertEquals(AccessTokenResult.InvalidCredentials, actual)
        }

    @Test
    fun `login failure with other error, returns AccessTokenResult#CommunicationError`() =
        runBlocking {
            coEvery {
                authApi.login(USERNAME_INPUT, PASSWORD_INPUT)
            } returns Err(AuthFailure.BadRequest)

            val actual = accessTokenRepository.login(USERNAME_INPUT, PASSWORD_INPUT)

            assertEquals(AccessTokenResult.CommunicationError, actual)
        }

    @Test
    fun `refresh on success, stores the accessToken`() = runBlocking {
        coEvery {
            authApi.refresh(REFRESH_TOKEN_INPUT)
        } returns Ok(AuthDomain(ACCESS_TOKEN, REFRESH_TOKEN))

        accessTokenRepository.refresh()

        verify { authProvider.accessToken = ACCESS_TOKEN }
    }

    @Test
    fun `refresh on success, stores the refreshToken`() = runBlocking {
        coEvery {
            authApi.refresh(REFRESH_TOKEN_INPUT)
        } returns Ok(AuthDomain(ACCESS_TOKEN, REFRESH_TOKEN))

        accessTokenRepository.refresh()

        verify { authProvider.refreshToken = REFRESH_TOKEN }
    }

    @Test
    fun `refresh on success, returns AccessTokenResult#Success`() = runBlocking {
        coEvery {
            authApi.refresh(REFRESH_TOKEN_INPUT)
        } returns Ok(AuthDomain(ACCESS_TOKEN, REFRESH_TOKEN))

        val result = accessTokenRepository.refresh()

        assertTrue(result is AccessTokenResult.Success)
    }

    @Test
    fun `refresh failure with InvalidCredentials, returns AccessTokenResult#InvalidCredentials`() =
        runBlocking {
            coEvery {
                authApi.refresh(REFRESH_TOKEN_INPUT)
            } returns Err(AuthFailure.InvalidCredentials)

            val actual = accessTokenRepository.refresh()

            assertEquals(AccessTokenResult.InvalidCredentials, actual)
        }

    @Test
    fun `refresh failure with other error, returns AccessTokenResult#CommunicationError`() =
        runBlocking {
            coEvery {
                authApi.refresh(REFRESH_TOKEN_INPUT)
            } returns Err(AuthFailure.BadRequest)

            val actual = accessTokenRepository.refresh()

            assertEquals(AccessTokenResult.CommunicationError, actual)
        }

    @Test
    fun `clear notifies provider to clear tokens`() {
        every { authProvider.clearAuth() } just runs

        accessTokenRepository.clear()

        verify { authProvider.clearAuth() }
    }
}
