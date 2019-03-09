package com.chesire.malime.repo

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.UserApi
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
    fun `getUser returns the UserModel`() = runBlocking {
        val expected = mockk<UserModel>()
        val mockDao = mockk<UserDao>()
        val mockApi = mockk<UserApi> {
            coEvery { getUserDetails() } coAnswers { Resource.Success(expected) }
        }

        val classUnderTest = UserRepository(mockDao, mockApi)
        val result = classUnderTest.getUser()

        when (result) {
            is Resource.Success -> assertEquals(expected, result.data)
            is Resource.Error -> fail()
        }
    }

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
}
