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

    private lateinit var auth: LocalAuth

    private lateinit var authProvider: AuthProvider

    @Before
    fun setup() {
        auth = mockk()
        authProvider = AuthProvider(auth)
    }

    @Test
    fun `accessToken gets token from correct auth`() {
        every { auth.accessToken } returns ACCESS_TOKEN

        authProvider.accessToken

        verify { auth.accessToken }
    }

    @Test
    fun `accessToken sets token in correct auth`() {
        every { auth.accessToken = ACCESS_TOKEN } just runs

        authProvider.accessToken = ACCESS_TOKEN

        verify { auth.accessToken = ACCESS_TOKEN }
    }

    @Test
    fun `refreshToken gets token from correct auth`() {
        every { auth.refreshToken } returns REFRESH_TOKEN

        authProvider.refreshToken

        verify { auth.refreshToken }
    }

    @Test
    fun `refreshToken sets token in correct auth`() {
        every { auth.refreshToken = REFRESH_TOKEN } just runs

        authProvider.refreshToken = REFRESH_TOKEN

        verify { auth.refreshToken = REFRESH_TOKEN }
    }

    @Test
    fun `clearAuth clears out all auth`() {
        every { auth.clear() } just runs

        authProvider.clearAuth()

        verify { auth.clear() }
    }
}
