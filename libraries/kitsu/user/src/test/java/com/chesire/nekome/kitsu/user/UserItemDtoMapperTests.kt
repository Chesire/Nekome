package com.chesire.nekome.kitsu.user

import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.user.dto.UserItemDto
import com.chesire.nekome.testing.createImageModel
import org.junit.Assert.assertEquals
import org.junit.Test

class UserItemDtoMapperTests {

    private val map = UserItemDtoMapper()

    @Test
    fun `toUserDomain converts UserItemDto to UserDomain`() {
        val avatarImageInput = createImageModel(
            ImageModel.ImageData("avatarTiny", 10, 5),
            ImageModel.ImageData("avatarSmall", 20, 10),
            ImageModel.ImageData("avatarMedium", 30, 15),
            ImageModel.ImageData("avatarLarge", 40, 20)
        )
        val coverImageInput = createImageModel(
            ImageModel.ImageData("coverTiny", 5, 10),
            ImageModel.ImageData("coverSmall", 10, 20),
            ImageModel.ImageData("coverMedium", 15, 30),
            ImageModel.ImageData("coverLarge", 20, 40)
        )
        val input = UserItemDto(
            10,
            UserItemDto.Attributes(
                "name",
                avatarImageInput,
                coverImageInput
            )
        )

        val output = map.toUserDomain(input)

        assertEquals(input.id, output.userId)
        assertEquals(input.attributes.name, output.name)
        assertEquals(input.attributes.avatar, output.avatar)
        assertEquals(input.attributes.coverImage, output.coverImage)
        assertEquals(Service.Kitsu, output.service)
    }
}
