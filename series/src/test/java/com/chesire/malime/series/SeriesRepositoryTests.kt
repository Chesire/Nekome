package com.chesire.malime.series

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.LibraryApi
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
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
    fun `anime observes the dao anime series`() {
        val mockDao = mockk<com.chesire.malime.database.dao.SeriesDao> {
            every { observe(SeriesType.Anime) } returns mockk { every { observeForever(any()) } just Runs }
        }
        val mockApi = mockk<LibraryApi>()
        val mockUser = mockk<com.chesire.malime.account.UserRepository>()

        val classUnderTest = com.chesire.malime.series.SeriesRepository(mockDao, mockApi, mockUser)
        classUnderTest.anime.observeForever(mockk<Observer<List<SeriesModel>>>())

        verify { mockDao.observe(SeriesType.Anime) }
    }

    @Test
    fun `manga observes the dao manga series`() {
        val mockDao = mockk<com.chesire.malime.database.dao.SeriesDao> {
            every { observe(SeriesType.Manga) } returns mockk { every { observeForever(any()) } just Runs }
        }
        val mockApi = mockk<LibraryApi>()
        val mockUser = mockk<com.chesire.malime.account.UserRepository>()

        val classUnderTest = com.chesire.malime.series.SeriesRepository(mockDao, mockApi, mockUser)
        classUnderTest.manga.observeForever(mockk<Observer<List<SeriesModel>>>())

        verify { mockDao.observe(SeriesType.Manga) }
    }

    @Test
    fun `series observes all the dao series`() {
        val mockDao = mockk<com.chesire.malime.database.dao.SeriesDao> {
            every { observe() } returns mockk { every { observeForever(any()) } just Runs }
        }
        val mockApi = mockk<LibraryApi>()
        val mockUser = mockk<com.chesire.malime.account.UserRepository>()

        val classUnderTest = com.chesire.malime.series.SeriesRepository(mockDao, mockApi, mockUser)
        classUnderTest.series.observeForever(mockk<Observer<List<SeriesModel>>>())

        verify { mockDao.observe() }
    }

    @Test
    fun `addAnime onSuccess saves to dao`() = runBlocking {
        val expected = Resource.Success<SeriesModel>(mockk())
        val mockDao = mockk<com.chesire.malime.database.dao.SeriesDao> {
            coEvery { insert(any<SeriesModel>()) } just Runs
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addAnime(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<com.chesire.malime.account.UserRepository> {
            coEvery { retrieveUserId() } returns 1
        }

        val classUnderTest = com.chesire.malime.series.SeriesRepository(mockDao, mockApi, mockUser)
        classUnderTest.addAnime(0, UserSeriesStatus.Current)

        coVerify { mockDao.insert(any<SeriesModel>()) }
    }

    @Test
    fun `addAnime onSuccess returns success`() = runBlocking {
        val expected = Resource.Success<SeriesModel>(mockk())
        val mockDao = mockk<com.chesire.malime.database.dao.SeriesDao> {
            coEvery { insert(any<SeriesModel>()) } just Runs
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addAnime(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<com.chesire.malime.account.UserRepository> {
            coEvery { retrieveUserId() } returns 1
        }

        val classUnderTest = com.chesire.malime.series.SeriesRepository(mockDao, mockApi, mockUser)
        val actual = classUnderTest.addAnime(0, UserSeriesStatus.Current)

        assertEquals(expected, actual)
    }

    @Test
    fun `addAnime onFailure returns failure`() = runBlocking {
        val expected = Resource.Error<SeriesModel>("Error")
        val mockDao = mockk<com.chesire.malime.database.dao.SeriesDao> {
            coEvery { insert(any<SeriesModel>()) } just Runs
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addAnime(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<com.chesire.malime.account.UserRepository> {
            coEvery { retrieveUserId() } returns 1
        }

        val classUnderTest = com.chesire.malime.series.SeriesRepository(mockDao, mockApi, mockUser)
        val actual = classUnderTest.addAnime(0, UserSeriesStatus.Current)

        assertEquals(expected, actual)
    }

    @Test
    fun `addManga onSuccess saves to dao`() = runBlocking {
        val expected = Resource.Success<SeriesModel>(mockk())
        val mockDao = mockk<com.chesire.malime.database.dao.SeriesDao> {
            coEvery { insert(any<SeriesModel>()) } just Runs
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addManga(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<com.chesire.malime.account.UserRepository> {
            coEvery { retrieveUserId() } returns 1
        }

        val classUnderTest = com.chesire.malime.series.SeriesRepository(mockDao, mockApi, mockUser)
        classUnderTest.addManga(0, UserSeriesStatus.Current)

        coVerify { mockDao.insert(any<SeriesModel>()) }
    }

    @Test
    fun `addManga onSuccess returns success`() = runBlocking {
        val expected = Resource.Success<SeriesModel>(mockk())
        val mockDao = mockk<com.chesire.malime.database.dao.SeriesDao> {
            coEvery { insert(any<SeriesModel>()) } just Runs
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addManga(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<com.chesire.malime.account.UserRepository> {
            coEvery { retrieveUserId() } returns 1
        }

        val classUnderTest = com.chesire.malime.series.SeriesRepository(mockDao, mockApi, mockUser)
        val actual = classUnderTest.addManga(0, UserSeriesStatus.Current)

        assertEquals(expected, actual)
    }

    @Test
    fun `addManga onFailure returns failure`() = runBlocking {
        val expected = Resource.Error<SeriesModel>("Error")
        val mockDao = mockk<com.chesire.malime.database.dao.SeriesDao> {
            coEvery { insert(any<SeriesModel>()) } just Runs
        }
        val mockApi = mockk<LibraryApi> {
            coEvery { addManga(any(), any(), any()) } returns expected
        }
        val mockUser = mockk<com.chesire.malime.account.UserRepository> {
            coEvery { retrieveUserId() } returns 1
        }

        val classUnderTest = com.chesire.malime.series.SeriesRepository(mockDao, mockApi, mockUser)
        val actual = classUnderTest.addManga(0, UserSeriesStatus.Current)

        assertEquals(expected, actual)
    }

    @Test
    fun `updateSeries on success updates the dao`() = runBlocking {
        val expected = mockk<SeriesModel>()
        val mockDao = mockk<com.chesire.malime.database.dao.SeriesDao> {
            coEvery { update(expected) } just Runs
        }
        val mockApi = mockk<LibraryApi> {
            coEvery {
                update(0, 0, UserSeriesStatus.Current)
            } coAnswers {
                Resource.Success(expected)
            }
        }
        val mockUser = mockk<com.chesire.malime.account.UserRepository>()

        val classUnderTest = com.chesire.malime.series.SeriesRepository(mockDao, mockApi, mockUser)
        classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current)

        coVerify { mockDao.update(expected) }
    }

    @Test
    fun `updateSeries on failure returns failure`() = runBlocking {
        val mockDao = mockk<com.chesire.malime.database.dao.SeriesDao>()
        val mockApi = mockk<LibraryApi> {
            coEvery {
                update(0, 0, UserSeriesStatus.Current)
            } coAnswers {
                Resource.Error("")
            }
        }
        val mockUser = mockk<com.chesire.malime.account.UserRepository>()

        val classUnderTest = com.chesire.malime.series.SeriesRepository(mockDao, mockApi, mockUser)
        val result = classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current)

        when (result) {
            is Resource.Success -> fail("Expected Resource.Error")
            is Resource.Error -> assertTrue(true)
        }
    }
}
