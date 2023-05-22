package com.chesire.nekome.app.login.syncing.core

import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.datasource.user.User
import com.chesire.nekome.datasource.user.UserDomain
import com.chesire.nekome.datasource.user.UserRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RetrieveAvatarUseCaseTest {

    private val userRepository = mockk<UserRepository>()
    private lateinit var retrieveAvatar: RetrieveAvatarUseCase

    @Before
    fun setup() {
        clearAllMocks()

        retrieveAvatar = RetrieveAvatarUseCase(userRepository)
    }

    @Test
    fun `When user repository returns no user found, Then empty string is returned`() = runTest {
        coEvery { userRepository.user } returns flowOf(User.NotFound)

        val result = retrieveAvatar()

        assertEquals("", result)
    }

    @Test
    fun `When user repository returns user found, Then users avatar url is retrieved`() = runTest {
        val expected = "largestURL"
        coEvery {
            userRepository.user
        } returns flowOf(
            User.Found(
                UserDomain(
                    userId = 0,
                    name = "name",
                    avatar = ImageModel(
                        tiny = ImageModel.empty.tiny,
                        small = ImageModel.empty.small,
                        medium = ImageModel.empty.medium,
                        large = ImageModel.ImageData(expected, 0, 0)
                    ),
                    coverImage = ImageModel.empty,
                    service = Service.Kitsu
                )
            )
        )

        val result = retrieveAvatar()

        assertEquals(expected, result)
    }
}
