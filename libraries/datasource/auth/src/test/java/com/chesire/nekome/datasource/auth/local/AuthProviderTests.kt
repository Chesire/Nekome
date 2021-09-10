package com.chesire.nekome.datasource.auth.local

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Before
import org.junit.Test

private const val ACCESS_TOKEN = "access_token"
private const val REFRESH_TOKEN = "refresh_token"

class AuthProviderTests {

    private lateinit var v1Auth: LocalAuthV1
    private lateinit var v2Auth: LocalAuthV2

    private lateinit var authProvider: AuthProvider

    @Before
    fun setup() {
        v1Auth = mockk {
            every { hasCredentials } returns false
        }
        v2Auth = mockk()
        authProvider = AuthProvider(v1Auth, v2Auth)
    }

    @Test
    fun `migration doesn't occur if no v1 credentials`() {
        v1Auth = mockk {
            every { hasCredentials } returns false
        }
        v2Auth = mockk()
        authProvider = AuthProvider(v1Auth, v2Auth)

        verify(exactly = 0) { v1Auth.accessToken }
    }

    @Test
    fun `migration copies over access token to v2`() {
        v1Auth = mockk {
            every { hasCredentials } returns true
            every { accessToken } returns ACCESS_TOKEN
            every { refreshToken } returns REFRESH_TOKEN
            every { clear() } just runs
        }
        v2Auth = mockk(relaxed = true)
        authProvider = AuthProvider(v1Auth, v2Auth)

        verify { v2Auth.accessToken = ACCESS_TOKEN }
    }

    @Test
    fun `migration copies over refresh token to v2`() {
        v1Auth = mockk {
            every { hasCredentials } returns true
            every { accessToken } returns ACCESS_TOKEN
            every { refreshToken } returns REFRESH_TOKEN
            every { clear() } just runs
        }
        v2Auth = mockk(relaxed = true)
        authProvider = AuthProvider(v1Auth, v2Auth)

        verify { v2Auth.refreshToken = REFRESH_TOKEN }
    }

    @Test
    fun `migration clears v1 after migration`() {
        v1Auth = mockk {
            every { hasCredentials } returns true
            every { accessToken } returns ACCESS_TOKEN
            every { refreshToken } returns REFRESH_TOKEN
            every { clear() } just runs
        }
        v2Auth = mockk(relaxed = true)
        authProvider = AuthProvider(v1Auth, v2Auth)

        verify { v1Auth.clear() }
    }

    @Test
    fun `accessToken gets token from correct auth`() {
        every { v2Auth.accessToken } returns ACCESS_TOKEN

        authProvider.accessToken

        verify { v2Auth.accessToken }
    }

    @Test
    fun `accessToken sets token in correct auth`() {
        every { v2Auth.accessToken = ACCESS_TOKEN } just runs

        authProvider.accessToken = ACCESS_TOKEN

        verify { v2Auth.accessToken = ACCESS_TOKEN }
    }

    @Test
    fun `refreshToken gets token from correct auth`() {
        every { v2Auth.refreshToken } returns REFRESH_TOKEN

        authProvider.refreshToken

        verify { v2Auth.refreshToken }
    }

    @Test
    fun `refreshToken sets token in correct auth`() {
        every { v2Auth.refreshToken = REFRESH_TOKEN } just runs

        authProvider.refreshToken = REFRESH_TOKEN

        verify { v2Auth.refreshToken = REFRESH_TOKEN }
    }

    @Test
    fun `clearAuth clears out all auth instances`() {
        every { v1Auth.clear() } just runs
        every { v2Auth.clear() } just runs

        authProvider.clearAuth()

        verify {
            v1Auth.clear()
            v2Auth.clear()
        }
    }
}
