package com.chesire.nekome.library

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.LibraryApi
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test

class SeriesRepositoryTests {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `addAnime onSuccess saves to dao`() = runBlocking {
        val expected = Resource.Success<SeriesModel>(mockk())
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<SeriesModel>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addAnime(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns 1
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        classUnderTest.addAnime(0, UserSeriesStatus.Current)

        coVerify { mockDao.insert(any<SeriesModel>()) }
    }

    @Test
    fun `addAnime onSuccess returns success`() = runBlocking {
        val expected = Resource.Success<SeriesModel>(mockk())
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<SeriesModel>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addAnime(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns 1
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        val actual = classUnderTest.addAnime(0, UserSeriesStatus.Current)

        assertEquals(expected, actual)
    }

    @Test
    fun `addAnime onFailure returns failure`() = runBlocking {
        val expected = Resource.Error<SeriesModel>("Error")
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<SeriesModel>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addAnime(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns 1
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        val actual = classUnderTest.addAnime(0, UserSeriesStatus.Current)

        assertEquals(expected, actual)
    }

    @Test
    fun `addManga onSuccess saves to dao`() = runBlocking {
        val expected = Resource.Success<SeriesModel>(mockk())
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<SeriesModel>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addManga(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns 1
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        classUnderTest.addManga(0, UserSeriesStatus.Current)

        coVerify { mockDao.insert(any<SeriesModel>()) }
    }

    @Test
    fun `addManga onSuccess returns success`() = runBlocking {
        val expected = Resource.Success<SeriesModel>(mockk())
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<SeriesModel>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addManga(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns 1
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        val actual = classUnderTest.addManga(0, UserSeriesStatus.Current)

        assertEquals(expected, actual)
    }

    @Test
    fun `addManga onFailure returns failure`() = runBlocking {
        val expected = Resource.Error<SeriesModel>("Error")
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<SeriesModel>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addManga(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns 1
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        val actual = classUnderTest.addManga(0, UserSeriesStatus.Current)

        assertEquals(expected, actual)
    }

    @Test
    fun `deleteSeries onSuccess removes from dao`() = runBlocking {
        val expected = mockk<SeriesModel> {
            every { userId } returns 5
        }
        val mockDao = mockk<SeriesDao> {
            coEvery { delete(any()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { delete(any()) } returns Resource.Success(Any())
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns 1
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        classUnderTest.deleteSeries(expected)

        coVerify { mockDao.delete(expected) }
    }

    @Test
    fun `deleteSeries onSuccess returns success`() = runBlocking {
        val expected = Resource.Success(Any())
        val mockDao = mockk<SeriesDao> {
            coEvery { delete(any()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { delete(any()) } returns expected
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns 1
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        val actual = classUnderTest.deleteSeries(
            mockk {
                every { userId } returns 5
            }
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `deleteSeries onFailure returns failure`() = runBlocking {
        val expected = Resource.Error<Any>("")
        val mockDao = mockk<SeriesDao> {
            coEvery { delete(any()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { delete(any()) } returns expected
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns 1
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        val actual = classUnderTest.deleteSeries(
            mockk {
                every { userId } returns 5
            }
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `refreshAnime on success updates the dao`() = runBlocking {
        val response = Resource.Success(listOf<SeriesModel>())
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<List<SeriesModel>>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { retrieveAnime(any()) } returns response
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns 1
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        classUnderTest.refreshAnime()

        coVerify { mockDao.insert(any<List<SeriesModel>>()) }
    }

    @Test
    fun `refreshAnime on failure doesn't update the dao`() = runBlocking {
        val response = Resource.Error<List<SeriesModel>>("")
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<List<SeriesModel>>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { retrieveAnime(any()) } returns response
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns 1
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        classUnderTest.refreshAnime()

        coVerify(exactly = 0) { mockDao.insert(any<List<SeriesModel>>()) }
    }

    @Test
    fun `refreshManga on success updates the dao`() = runBlocking {
        val response = Resource.Success(listOf<SeriesModel>())
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<List<SeriesModel>>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { retrieveManga(any()) } returns response
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns 1
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        classUnderTest.refreshManga()

        coVerify { mockDao.insert(any<List<SeriesModel>>()) }
    }

    @Test
    fun `refreshManga on failure doesn't update the dao`() = runBlocking {
        val response = Resource.Error<List<SeriesModel>>("")
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<List<SeriesModel>>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { retrieveManga(any()) } returns response
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns 1
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        classUnderTest.refreshManga()

        coVerify(exactly = 0) { mockDao.insert(any<List<SeriesModel>>()) }
    }

    @Test
    fun `updateSeries on success updates the dao`() = runBlocking {
        val expected = mockk<SeriesModel>()
        val mockDao = mockk<SeriesDao> {
            coEvery { update(expected) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery {
                update(0, 0, UserSeriesStatus.Current)
            } coAnswers {
                Resource.Success(expected)
            }
        }
        val mockUser = mockk<UserProvider>()

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current)

        coVerify { mockDao.update(expected) }
    }

    @Test
    fun `updateSeries on failure returns failure`() = runBlocking {
        val mockDao = mockk<SeriesDao> {
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery {
                update(0, 0, UserSeriesStatus.Current)
            } coAnswers {
                Resource.Error("")
            }
        }
        val mockUser = mockk<UserProvider>()

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        val result = classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current)

        when (result) {
            is Resource.Success -> fail("Expected Resource.Error")
            is Resource.Error -> assertTrue(true)
        }
    }
}
