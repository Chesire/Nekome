package com.chesire.malime.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.db.SeriesDao
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test

class SeriesRepositoryTests {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `anime observes the dao anime series`() {
        val mockDao = mockk<SeriesDao> {
            every { observe(SeriesType.Anime) } returns mockk { every { observeForever(any()) } just Runs }
        }
        val mockApi = mockk<LibraryApi>()
        val mockUser = mockk<UserRepository>()

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        classUnderTest.anime.observeForever(mockk<Observer<List<SeriesModel>>>())

        verify { mockDao.observe(SeriesType.Anime) }
    }

    @Test
    fun `manga observes the dao manga series`() {
        val mockDao = mockk<SeriesDao> {
            every { observe(SeriesType.Manga) } returns mockk { every { observeForever(any()) } just Runs }
        }
        val mockApi = mockk<LibraryApi>()
        val mockUser = mockk<UserRepository>()

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        classUnderTest.manga.observeForever(mockk<Observer<List<SeriesModel>>>())

        verify { mockDao.observe(SeriesType.Manga) }
    }

    @Test
    fun `updateSeries on success updates the dao`() = runBlocking {
        val expected = mockk<SeriesModel>()
        val mockDao = mockk<SeriesDao> {
            coEvery { update(expected) } just Runs
        }
        val mockApi = mockk<LibraryApi> {
            coEvery {
                update(0, 0, UserSeriesStatus.Current)
            } coAnswers {
                Resource.Success(expected)
            }
        }
        val mockUser = mockk<UserRepository>()

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current)

        coVerify { mockDao.update(expected) }
    }

    @Test
    fun `updateSeries on failure returns failure`() = runBlocking {
        val mockDao = mockk<SeriesDao>()
        val mockApi = mockk<LibraryApi> {
            coEvery {
                update(0, 0, UserSeriesStatus.Current)
            } coAnswers {
                Resource.Error("")
            }
        }
        val mockUser = mockk<UserRepository>()

        val classUnderTest = SeriesRepository(mockDao, mockApi, mockUser)
        val result = classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current)

        when (result) {
            is Resource.Success -> fail("Expected Resource.Error")
            is Resource.Error -> assertTrue(true)
        }
    }
}
