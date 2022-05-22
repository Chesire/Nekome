package com.chesire.nekome.kitsu.user

import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.user.dto.UserItemDto
import com.chesire.nekome.kitsu.user.dto.UserResponseDto
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.net.UnknownHostException
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class KitsuUserTests {

    private val mapper = UserItemDtoMapper()

    @Test
    fun `getUserDetails failure response returns Resource#Error with errorBody`() = runBlocking {
        val expected = "errorBodyString"

        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns expected
        }
        val mockResponse = mockk<Response<UserResponseDto>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockResponseBody
            every { code() } returns 0
        }
        val mockService = mockk<KitsuUserService> {
            coEvery {
                getUserDetailsAsync()
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuUser(mockService, mapper)
        val actual = classUnderTest.getUserDetails()

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `getUserDetails failure response returns Resource#Error with message if no error`() =
        runBlocking {
            val expected = "responseBodyString"

            val mockResponse = mockk<Response<UserResponseDto>> {
                every { isSuccessful } returns false
                every { errorBody() } returns null
                every { message() } returns expected
                every { code() } returns 0
            }
            val mockService = mockk<KitsuUserService> {
                coEvery {
                    getUserDetailsAsync()
                } coAnswers {
                    mockResponse
                }
            }

            val classUnderTest = KitsuUser(mockService, mapper)
            val actual = classUnderTest.getUserDetails()

            when (actual) {
                is Resource.Success -> error("Test has failed")
                is Resource.Error -> assertEquals(expected, actual.msg)
            }
        }

    @Test
    fun `getUserDetails successful response with no body returns Resource#Error`() = runBlocking {
        val expected = "Response body is null"

        val mockResponse = mockk<Response<UserResponseDto>> {
            every { isSuccessful } returns true
            every { body() } returns null
            every { message() } returns expected
        }
        val mockService = mockk<KitsuUserService> {
            coEvery {
                getUserDetailsAsync()
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuUser(mockService, mapper)
        val actual = classUnderTest.getUserDetails()

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `getUserDetails successful response with body returns Resource#Success`() = runBlocking {
        val expected = UserResponseDto(
            listOf(
                UserItemDto(
                    0,
                    UserItemDto.Attributes(
                        "name",
                        ImageModel.empty,
                        ImageModel.empty
                    )
                )
            )
        )

        val mockResponse = mockk<Response<UserResponseDto>> {
            every { isSuccessful } returns true
            every { body() } returns expected
        }
        val mockService = mockk<KitsuUserService> {
            coEvery {
                getUserDetailsAsync()
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuUser(mockService, mapper)
        val actual = classUnderTest.getUserDetails()

        when (actual) {
            is Resource.Success -> {
                /* Pass */
            }
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `on thrown exception return Resource#Error`() = runBlocking {
        val mockService = mockk<KitsuUserService> {
            coEvery { getUserDetailsAsync() } throws UnknownHostException()
        }

        val classUnderTest = KitsuUser(mockService, mapper)
        val result = classUnderTest.getUserDetails()

        when (result) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertTrue(true)
        }
    }
}
