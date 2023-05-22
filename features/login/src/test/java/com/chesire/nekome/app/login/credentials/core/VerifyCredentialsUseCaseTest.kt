package com.chesire.nekome.app.login.credentials.core

import com.chesire.nekome.datasource.auth.AccessTokenRepository
import com.chesire.nekome.datasource.auth.AccessTokenResult
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class VerifyCredentialsUseCaseTest {

    private val auth = mockk<AccessTokenRepository>()
    private lateinit var verifyCredentials: VerifyCredentialsUseCase

    @Before
    fun setup() {
        clearAllMocks()
        verifyCredentials = VerifyCredentialsUseCase(auth)
    }

    @Test
    fun `Given username is invalid, Then UsernameInvalid error returned`() = runTest {
        val result = verifyCredentials("", "password")

        check(result is Err)
        assertEquals(VerifyCredentialsFailure.UsernameInvalid, result.error)
    }

    @Test
    fun `Given password is invalid, Then UsernameInvalid error returned`() = runTest {
        val result = verifyCredentials("username", "")

        check(result is Err)
        assertEquals(VerifyCredentialsFailure.PasswordInvalid, result.error)
    }

    @Test
    fun `Given communication error, Then NetworkError error returned`() = runTest {
        coEvery { auth.login(any(), any()) } returns AccessTokenResult.CommunicationError

        val result = verifyCredentials("username", "password")

        check(result is Err)
        assertEquals(VerifyCredentialsFailure.NetworkError, result.error)
    }

    @Test
    fun `Given invalid credentials, Then InvalidCredentials error returned`() = runTest {
        coEvery { auth.login(any(), any()) } returns AccessTokenResult.InvalidCredentials

        val result = verifyCredentials("username", "password")

        check(result is Err)
        assertEquals(VerifyCredentialsFailure.InvalidCredentials, result.error)
    }

    @Test
    fun `Given successful login, Then Ok is returned`() = runTest {
        coEvery { auth.login(any(), any()) } returns AccessTokenResult.Success

        val result = verifyCredentials("username", "password")

        assertTrue(result is Ok)
    }
}
