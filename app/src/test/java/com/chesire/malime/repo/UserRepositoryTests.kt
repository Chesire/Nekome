package com.chesire.malime.repo

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.UserApi
import com.chesire.malime.core.flags.Service
import com.chesire.malime.core.models.UserModel
import com.chesire.malime.db.UserDao
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class UserRepositoryTests {
    @Test
    fun `insertUser pushes UserModel into Dao`() = runBlocking {
        val expected = mockk<UserModel>()
        val mockDao = mockk<UserDao> {
            coEvery { insert(expected) } coAnswers { }
        }
        val mockApi = mockk<UserApi>()

        val classUnderTest = UserRepository(mockDao, mockApi)
        classUnderTest.insertUser(expected)

        verify {
            runBlocking {
                mockDao.insert(expected)
            }
        }
    }

    @Test
    fun `retrieveUser returns the UserModel from dao`() = runBlocking {
        val expected = mockk<UserModel>()
        val mockDao = mockk<UserDao> {
            coEvery { retrieve(Service.Kitsu) } coAnswers { expected }
        }
        val mockApi = mockk<UserApi>()

        val classUnderTest = UserRepository(mockDao, mockApi)

        assertEquals(expected, classUnderTest.retrieveUser())
    }

    @Test
    fun `retrieveUserId gets just the id of the user from dao`() = runBlocking {
        val expected = 133
        val mockDao = mockk<UserDao> {
            coEvery { retrieveUserId(Service.Kitsu) } coAnswers { expected }
        }
        val mockApi = mockk<UserApi>()

        val classUnderTest = UserRepository(mockDao, mockApi)

        assertEquals(expected, classUnderTest.retrieveUserId())
    }

    @Test
    fun `retrieveRemoteUser returns the UserModel from api`() = runBlocking {
        val expected = mockk<UserModel>()
        val mockDao = mockk<UserDao>()
        val mockApi = mockk<UserApi> {
            coEvery { getUserDetails() } coAnswers { Resource.Success(expected) }
        }

        val classUnderTest = UserRepository(mockDao, mockApi)
        val result = classUnderTest.retrieveRemoteUser()

        when (result) {
            is Resource.Success -> assertEquals(expected, result.data)
            is Resource.Error -> fail()
        }
    }
}
