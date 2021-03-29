package com.chesire.nekome.datasource.user

import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.database.entity.UserEntity
import com.chesire.nekome.testing.createImageModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserMapperTests {

    private lateinit var map: UserMapper

    @Before
    fun setup() {
        map = UserMapper()
    }

    @Test
    fun `toUserModel converts UserDomain to UserEntity`() {
        val input = UserDomain(
            15,
            "name",
            createImageModel(
                ImageModel.ImageData("avatarTiny", 10, 5),
                ImageModel.ImageData("avatarSmall", 20, 10),
                ImageModel.ImageData("avatarMedium", 30, 15),
                ImageModel.ImageData("avatarLarge", 40, 20),
            ),
            createImageModel(
                ImageModel.ImageData("coverTiny", 5, 10),
                ImageModel.ImageData("coverSmall", 10, 20),
                ImageModel.ImageData("coverMedium", 15, 30),
                ImageModel.ImageData("coverLarge", 20, 40),
            ),
            Service.Kitsu
        )

        val output = map.toUserEntity(input)

        assertEquals(input.userId, output.userId)
        assertEquals(input.name, output.name)
        assertEquals(input.avatar, output.avatar)
        assertEquals(input.coverImage, output.coverImage)
        assertEquals(input.service, output.service)
    }

    @Test
    fun `toUserDomain converts UserEntity to UserDomain`() {
        val input = UserEntity(
            15,
            "name",
            createImageModel(
                ImageModel.ImageData("avatarTiny", 10, 5),
                ImageModel.ImageData("avatarSmall", 20, 10),
                ImageModel.ImageData("avatarMedium", 30, 15),
                ImageModel.ImageData("avatarLarge", 40, 20),
            ),
            createImageModel(
                ImageModel.ImageData("coverTiny", 5, 10),
                ImageModel.ImageData("coverSmall", 10, 20),
                ImageModel.ImageData("coverMedium", 15, 30),
                ImageModel.ImageData("coverLarge", 20, 40),
            ),
            Service.Kitsu
        )

        val output = map.toUserDomain(input)

        assertEquals(input.userId, output.userId)
        assertEquals(input.name, output.name)
        assertEquals(input.avatar, output.avatar)
        assertEquals(input.coverImage, output.coverImage)
    }
}
