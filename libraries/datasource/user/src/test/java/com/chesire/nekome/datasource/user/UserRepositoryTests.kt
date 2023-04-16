package com.chesire.nekome.datasource.user

import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.database.dao.UserDao
import com.chesire.nekome.testing.createUserEntity
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class UserRepositoryTests {

    private val map = UserMapper()

    @Test
    fun `user with a valid model returns User#Found`() = runBlocking {
        val mockDao = mockk<UserDao> {
            coEvery { insert(any()) } just Runs
            every { getUser(Service.Kitsu) } returns flowOf(createUserEntity())
        }
        val classUnderTest = UserRepository(mockDao, mockk(), UserMapper())

        lateinit var result: User
        classUnderTest.user.collect {
            result = it
        }

        assertTrue(result is User.Found)
    }

    @Test
    fun `user without a valid model returns User#NotFound`() = runBlocking {
        val mockDao = mockk<UserDao> {
            coEvery { insert(any()) } just Runs
            every { getUser(Service.Kitsu) } returns flowOf(null)
        }
        val classUnderTest = UserRepository(mockDao, mockk(), UserMapper())

        lateinit var result: User
        classUnderTest.user.collect {
            result = it
        }

        assertTrue(result is User.NotFound)
    }

    @Test
    fun `refreshUser stores userModel on success`() = runBlocking {
        val mockDao = mockk<UserDao> {
            coEvery { insert(any()) } just Runs
            every { getUser(Service.Kitsu) } returns mockk()
        }
        val mockApi = mockk<com.chesire.nekome.datasource.user.remote.UserApi> {
            coEvery {
                getUserDetails()
            } coAnswers {
                Resource.Success(
                    UserDomain(
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
        val mockApi = mockk<com.chesire.nekome.datasource.user.remote.UserApi> {
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
        val mockApi = mockk<com.chesire.nekome.datasource.user.remote.UserApi>()

        val classUnderTest = UserRepository(mockDao, mockApi, map)

        assertEquals(expected, classUnderTest.retrieveUserId())
    }
}
