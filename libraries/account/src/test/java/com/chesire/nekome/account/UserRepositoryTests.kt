package com.chesire.nekome.account

import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.database.dao.UserDao
import com.chesire.nekome.user.api.UserApi
import com.chesire.nekome.user.api.UserEntity
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class UserRepositoryTests {
    private val map = UserEntityMapper()

    @Test
    fun `refreshUser stores userModel on success`() = runBlocking {
        val mockDao = mockk<UserDao> {
            coEvery { insert(any()) } just Runs
            every { getUser(Service.Kitsu) } returns mockk()
        }
        val mockApi = mockk<UserApi> {
            coEvery {
                getUserDetails()
            } coAnswers {
                Resource.Success(
                    UserEntity(
                        0,
                        "name",
                        ImageModel.empty,
                        ImageModel.empty,
                        Service.Kitsu
                    )
                )
            }
        }

        val classUnderTest = UserRepository(mockDao, mockApi, map)

        when (classUnderTest.refreshUser()) {
            is Resource.Success -> coVerify { mockDao.insert(any()) }
            is Resource.Error -> fail()
        }
    }

    @Test
    fun `refreshUser returns the response from api`() = runBlocking {
        val expected = "error"
        val mockDao = mockk<UserDao> {
            every { getUser(Service.Kitsu) } returns mockk()
        }
        val mockApi = mockk<UserApi> {
            coEvery { getUserDetails() } coAnswers { Resource.Error(expected) }
        }

        val classUnderTest = UserRepository(mockDao, mockApi, map)

        when (val result = classUnderTest.refreshUser()) {
            is Resource.Success -> fail()
            is Resource.Error -> assertEquals(expected, result.msg)
        }
    }

    @Test
    fun `retrieveUserId gets just the id of the user from dao`() = runBlocking {
        val expected = 133
        val mockDao = mockk<UserDao> {
            coEvery { retrieveUserId(Service.Kitsu) } coAnswers { expected }
            every { getUser(Service.Kitsu) } returns mockk()
        }
        val mockApi = mockk<UserApi>()

        val classUnderTest = UserRepository(mockDao, mockApi, map)

        assertEquals(expected, classUnderTest.retrieveUserId())
    }
}
