package com.chesire.malime.kitsu.api.user

import com.chesire.malime.server.flags.RatingSystem
import com.chesire.malime.server.models.ImageModel
import org.junit.Assert.assertEquals
import org.junit.Test

class UserModelAdapterTests {
    @Test
    fun `userModelFromUserDetails converts to UserModel`() {
        val responseData = listOf(
            GetUserDetailsResponse.UserDetailsData(
                0,
                GetUserDetailsResponse.UserDetailsData.UserDetailsAttributes(
                    "name",
                    "slug",
                    RatingSystem.Simple,
                    ImageModel.empty,
                    ImageModel.empty
                )
            )
        )
        val response = GetUserDetailsResponse(responseData)

        val classUnderTest = UserModelAdapter()
        val actual = classUnderTest.userModelFromUserDetails(response)

        assertEquals(0, actual.userId)
        assertEquals("name", actual.name)
        assertEquals(ImageModel.empty, actual.avatar)
        assertEquals(ImageModel.empty, actual.coverImage)
    }

    @Test
    fun `userModelFromUserDetails null avatar sets empty ImageModel`() {
        val responseData = listOf(
            GetUserDetailsResponse.UserDetailsData(
                0,
                GetUserDetailsResponse.UserDetailsData.UserDetailsAttributes(
                    "name",
                    "slug",
                    RatingSystem.Simple,
                    null,
                    ImageModel.empty
                )
            )
        )
        val response = GetUserDetailsResponse(responseData)

        val classUnderTest = UserModelAdapter()
        val actual = classUnderTest.userModelFromUserDetails(response)

        assertEquals(ImageModel.empty, actual.avatar)
    }

    @Test
    fun `userModelFromUserDetails null coverImage sets empty ImageModel`() {
        val responseData = listOf(
            GetUserDetailsResponse.UserDetailsData(
                0,
                GetUserDetailsResponse.UserDetailsData.UserDetailsAttributes(
                    "name",
                    "slug",
                    RatingSystem.Simple,
                    null,
                    null
                )
            )
        )
        val response = GetUserDetailsResponse(responseData)

        val classUnderTest = UserModelAdapter()
        val actual = classUnderTest.userModelFromUserDetails(response)

        assertEquals(ImageModel.empty, actual.coverImage)
    }
}
