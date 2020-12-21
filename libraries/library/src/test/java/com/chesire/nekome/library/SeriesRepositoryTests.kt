package com.chesire.nekome.library

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.database.entity.SeriesEntity
import com.chesire.nekome.library.api.LibraryApi
import com.chesire.nekome.library.api.LibraryDomain
import com.chesire.nekome.testing.createSeriesDomain
import io.mockk.CapturingSlot
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

    private val map = LibraryDomainMapper()

    @Test
    fun `addAnime onSuccess saves to dao`() = runBlocking {
        val expected = Resource.Success(createLibraryDomain())
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<SeriesEntity>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addAnime(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns UserProvider.UserIdResult.Success(1)
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser, map)
        classUnderTest.addAnime(0, UserSeriesStatus.Current)

        coVerify { mockDao.insert(any<SeriesEntity>()) }
    }

    @Test
    fun `addAnime onSuccess returns success`() = runBlocking {
        val expected = Resource.Success(createLibraryDomain())
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<SeriesEntity>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addAnime(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns UserProvider.UserIdResult.Success(1)
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser, map)
        val actual = classUnderTest.addAnime(0, UserSeriesStatus.Current)

        assertTrue(actual is Resource.Success)
    }

    @Test
    fun `addAnime onFailure returns failure`() = runBlocking {
        val expected = Resource.Error<LibraryDomain>("Error")
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<SeriesEntity>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addAnime(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns UserProvider.UserIdResult.Success(1)
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser, map)
        val actual = classUnderTest.addAnime(0, UserSeriesStatus.Current)

        assertTrue(actual is Resource.Error)
    }

    @Test
    fun `addManga onSuccess saves to dao`() = runBlocking {
        val expected = Resource.Success(createLibraryDomain())
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<SeriesEntity>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addManga(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns UserProvider.UserIdResult.Success(1)
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser, map)
        classUnderTest.addManga(0, UserSeriesStatus.Current)

        coVerify { mockDao.insert(any<SeriesEntity>()) }
    }

    @Test
    fun `addManga onSuccess returns success`() = runBlocking {
        val expected = Resource.Success(createLibraryDomain())
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<SeriesEntity>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addManga(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns UserProvider.UserIdResult.Success(1)
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser, map)
        val actual = classUnderTest.addManga(0, UserSeriesStatus.Current)

        assertTrue(actual is Resource.Success)
    }

    @Test
    fun `addManga onFailure returns failure`() = runBlocking {
        val expected = Resource.Error<LibraryDomain>("Error")
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<SeriesEntity>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addManga(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns UserProvider.UserIdResult.Success(1)
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser, map)
        val actual = classUnderTest.addManga(0, UserSeriesStatus.Current)

        assertTrue(actual is Resource.Error)
    }

    @Test
    fun `deleteSeries onSuccess removes from dao`() = runBlocking {
        val slot = CapturingSlot<Int>()
        val mockDao = mockk<SeriesDao> {
            coEvery { delete(any()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { delete(capture(slot)) } returns Resource.Success(Any())
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns UserProvider.UserIdResult.Success(1)
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser, map)
        classUnderTest.deleteSeries(createSeriesDomain(userId = 5))

        coVerify { mockDao.delete(any()) }
        assertEquals(5, slot.captured)
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
            coEvery { provideUserId() } returns UserProvider.UserIdResult.Success(1)
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser, map)
        val actual = classUnderTest.deleteSeries(createSeriesDomain(userId = 5))

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
            coEvery { provideUserId() } returns UserProvider.UserIdResult.Success(1)
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser, map)
        val actual = classUnderTest.deleteSeries(
            mockk {
                every { userId } returns 5
            }
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `refreshAnime on success updates the dao`() = runBlocking {
        val response = Resource.Success(listOf<LibraryDomain>())
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<List<SeriesEntity>>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { retrieveAnime(any()) } returns response
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns UserProvider.UserIdResult.Success(1)
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser, map)
        classUnderTest.refreshAnime()

        coVerify { mockDao.insert(any<List<SeriesEntity>>()) }
    }

    @Test
    fun `refreshAnime on failure doesn't update the dao`() = runBlocking {
        val response = Resource.Error<List<LibraryDomain>>("")
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<List<SeriesEntity>>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { retrieveAnime(any()) } returns response
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns UserProvider.UserIdResult.Success(1)
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser, map)
        classUnderTest.refreshAnime()

        coVerify(exactly = 0) { mockDao.insert(any<List<SeriesEntity>>()) }
    }

    @Test
    fun `refreshManga on success updates the dao`() = runBlocking {
        val response = Resource.Success(listOf<LibraryDomain>())
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<List<SeriesEntity>>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { retrieveManga(any()) } returns response
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns UserProvider.UserIdResult.Success(1)
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser, map)
        classUnderTest.refreshManga()

        coVerify { mockDao.insert(any<List<SeriesEntity>>()) }
    }

    @Test
    fun `refreshManga on failure doesn't update the dao`() = runBlocking {
        val response = Resource.Error<List<LibraryDomain>>("")
        val mockDao = mockk<SeriesDao> {
            coEvery { insert(any<List<SeriesEntity>>()) } just Runs
            every { getSeries() } returns mockk()
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { retrieveManga(any()) } returns response
        }
        val mockUser = mockk<UserProvider> {
            coEvery { provideUserId() } returns UserProvider.UserIdResult.Success(1)
        }

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser, map)
        classUnderTest.refreshManga()

        coVerify(exactly = 0) { mockDao.insert(any<List<SeriesEntity>>()) }
    }

    @Test
    fun `updateSeries on success updates the dao`() = runBlocking {
        val expected = createLibraryDomain()
        val mockDao = mockk<SeriesDao> {
            coEvery { update(any()) } just Runs
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

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser, map)
        classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current)

        coVerify { mockDao.update(any()) }
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

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser, map)
        val result = classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current)

        when (result) {
            is Resource.Success -> fail("Expected Resource.Error")
            is Resource.Error -> assertTrue(true)
        }
    }

    private fun createLibraryDomain() =
        LibraryDomain(
            0,
            0,
            SeriesType.Anime,
            Subtype.Unknown,
            "slug",
            "title",
            SeriesStatus.Unknown,
            UserSeriesStatus.Unknown,
            0,
            0,
            ImageModel.empty,
            "startDate",
            "endDate"
        )
}
