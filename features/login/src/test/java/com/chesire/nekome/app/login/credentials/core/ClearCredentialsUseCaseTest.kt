package com.chesire.nekome.app.login.credentials.core

import com.chesire.nekome.datasource.auth.AccessTokenRepository
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ClearCredentialsUseCaseTest {

    private val authRepo = mockk<AccessTokenRepository>(relaxed = true)
    private lateinit var clearCredentials: ClearCredentialsUseCase

    @Before
    fun setup() {
        clearAllMocks()
        clearCredentials = ClearCredentialsUseCase(authRepo)
    }

    @Test
    fun `UseCase invoke clears the auth repo`() {
        clearCredentials()

        verify { authRepo.clear() }
    }
}
