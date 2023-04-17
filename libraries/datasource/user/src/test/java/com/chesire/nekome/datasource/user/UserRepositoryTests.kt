package com.chesire.nekome.datasource.user

import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.core.models.ErrorDomain
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.database.dao.UserDao
import com.chesire.nekome.datasource.user.remote.UserApi
import com.chesire.nekome.testing.createUserEntity
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.getError
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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
        val mockApi = mockk<UserApi> {
            coEvery {
                getUserDetails()
            } coAnswers {
                Ok(
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
        classUnderTest.refreshUser()

        coVerify { mockDao.insert(any()) }
    }

    @Test
    fun `refreshUser returns the response from api`() = runBlocking {
        val expected = "error"
        val mockDao = mockk<UserDao> {
            every { getUser(Service.Kitsu) } returns mockk()
        }
        val mockApi = mockk<UserApi> {
            coEvery { getUserDetails() } coAnswers { Err(ErrorDomain(expected, 0)) }
        }

        val classUnderTest = UserRepository(mockDao, mockApi, map)
        val result = classUnderTest.refreshUser().getError()

        assertNotNull(result)
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
