package com.chesire.malime.repo

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.UserApi
import com.chesire.malime.core.flags.Service
import com.chesire.malime.core.models.UserModel
import com.chesire.malime.db.UserDao
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
            every { observe(Service.Kitsu) } returns mockk()
        }
        val mockApi = mockk<UserApi> {
            coEvery { getUserDetails() } coAnswers { Resource.Success(expected) }
        }

        val classUnderTest = UserRepository(mockDao, mockApi)
        val result = classUnderTest.refreshUser()

        when (result) {
            is Resource.Success -> coVerify { mockDao.insert(expected) }
            is Resource.Error -> fail()
        }
    }

    @Test
    fun `refreshUser returns the response from api`() = runBlocking {
        val expected = "error"
        val mockDao = mockk<UserDao> {
            every { observe(Service.Kitsu) } returns mockk()
        }
        val mockApi = mockk<UserApi> {
            coEvery { getUserDetails() } coAnswers { Resource.Error(expected) }
        }

        val classUnderTest = UserRepository(mockDao, mockApi)
        val result = classUnderTest.refreshUser()

        when (result) {
            is Resource.Success -> fail()
            is Resource.Error -> assertEquals(expected, result.msg)
        }
    }

    @Test
    fun `retrieveUserId gets just the id of the user from dao`() = runBlocking {
        val expected = 133
        val mockDao = mockk<UserDao> {
            coEvery { retrieveUserId(Service.Kitsu) } coAnswers { expected }
            every { observe(Service.Kitsu) } returns mockk()
        }
        val mockApi = mockk<UserApi>()

        val classUnderTest = UserRepository(mockDao, mockApi)

        assertEquals(expected, classUnderTest.retrieveUserId())
    }
}
