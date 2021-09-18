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

    private lateinit var v2Auth: LocalAuthV2

    private lateinit var authProvider: AuthProvider

    @Before
    fun setup() {
        v2Auth = mockk()
        authProvider = AuthProvider(v2Auth)
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
    fun `clearAuth clears out all auth`() {
        every { v2Auth.clear() } just runs

        authProvider.clearAuth()

        verify { v2Auth.clear() }
    }
}
