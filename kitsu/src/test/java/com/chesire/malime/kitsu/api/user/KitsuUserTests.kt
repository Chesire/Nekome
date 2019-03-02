package com.chesire.malime.kitsu.api.user

import com.chesire.malime.core.Resource
import com.chesire.malime.core.flags.Service
import com.chesire.malime.core.models.ImageModel
import com.chesire.malime.core.models.UserModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Test
import retrofit2.Response

class KitsuUserTests {
    @Test
    fun `getUserDetails failure response returns Resource#Error with errorBody`() = runBlocking {
        val expected = "errorBodyString"

        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns expected
        }
        val mockResponse = mockk<Response<UserModel>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockResponseBody
            every { code() } returns 0
        }
        val mockService = mockk<KitsuUserService> {
            every {
                getUserDetailsAsync()
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuUser(mockService)
        val actual = classUnderTest.getUserDetails()

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> Assert.assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `getUserDetails failure response returns Resource#Error with message if no error`() =
        runBlocking {
            val expected = "responseBodyString"

            val mockResponse = mockk<Response<UserModel>> {
                every { isSuccessful } returns false
                every { errorBody() } returns null
                every { message() } returns expected
                every { code() } returns 0
            }
            val mockService = mockk<KitsuUserService> {
                every {
                    getUserDetailsAsync()
                } returns async {
                    mockResponse
                }
            }

            val classUnderTest = KitsuUser(mockService)
            val actual = classUnderTest.getUserDetails()

            when (actual) {
                is Resource.Success -> error("Test has failed")
                is Resource.Error -> Assert.assertEquals(expected, actual.msg)
            }
        }

    @Test
    fun `getUserDetails successful response with no body returns Resource#Error`() = runBlocking {
        val expected = "Response body is null"

        val mockResponse = mockk<Response<UserModel>> {
            every { isSuccessful } returns true
            every { body() } returns null
            every { message() } returns expected
        }
        val mockService = mockk<KitsuUserService> {
            every {
                getUserDetailsAsync()
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuUser(mockService)
        val actual = classUnderTest.getUserDetails()

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> Assert.assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `getUserDetails successful response with body returns Resource#Success`() = runBlocking {
        val expected = UserModel(0, "name", ImageModel.empty, ImageModel.empty, Service.Kitsu)

        val mockResponse = mockk<Response<UserModel>> {
            every { isSuccessful } returns true
            every { body() } returns expected
        }
        val mockService = mockk<KitsuUserService> {
            every {
                getUserDetailsAsync()
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuUser(mockService)
        val actual = classUnderTest.getUserDetails()

        when (actual) {
            is Resource.Success -> Assert.assertEquals(expected, actual.data)
            is Resource.Error -> error("Test has failed")
        }
    }
}
