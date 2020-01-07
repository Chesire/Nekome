package com.chesire.nekome.account

import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.core.models.UserModel
import com.chesire.nekome.database.dao.UserDao
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.UserApi
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
    @Test
    fun `refreshUser stores userModel on success`() = runBlocking {
        val expected = mockk<UserModel>()
        val mockDao = mockk<UserDao> {
            coEvery { insert(expected) } just Runs
            every { getUser(Service.Kitsu) } returns mockk()
        }
        val mockApi = mockk<UserApi> {
            coEvery { getUserDetails() } coAnswers { Resource.Success(expected) }
        }

        val classUnderTest = UserRepository(mockDao, mockApi)

        when (classUnderTest.refreshUser()) {
            is Resource.Success -> coVerify { mockDao.insert(expected) }
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

        val classUnderTest = UserRepository(mockDao, mockApi)

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

        val classUnderTest = UserRepository(mockDao, mockApi)

        assertEquals(expected, classUnderTest.retrieveUserId())
    }
}
