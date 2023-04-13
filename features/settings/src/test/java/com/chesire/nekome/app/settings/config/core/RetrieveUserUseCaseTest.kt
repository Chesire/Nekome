package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.datasource.user.User
import com.chesire.nekome.datasource.user.UserRepository
import com.chesire.nekome.testing.createUserDomain
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RetrieveUserUseCaseTest {

    private val userRepository = mockk<UserRepository>()
    private lateinit var retrieveUser: RetrieveUserUseCase

    @Before
    fun setup() {
        clearAllMocks()

        retrieveUser = RetrieveUserUseCase(userRepository)
    }

    @Test
    fun `invoking returns a flow of the User object`() = runBlocking {
        val user = User.Found(createUserDomain())
        coEvery { userRepository.user } returns flowOf(user)

        val result = retrieveUser()

        assertEquals(user, result.first())
    }
}
