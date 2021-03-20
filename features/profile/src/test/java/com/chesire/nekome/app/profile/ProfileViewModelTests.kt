package com.chesire.nekome.app.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.datasource.series.SeriesRepository
import com.chesire.nekome.datasource.user.UserRepository
import com.chesire.nekome.testing.CoroutinesMainDispatcherRule
import com.chesire.nekome.testing.createSeriesDomain
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTests {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutinesMainDispatcherRule()

    @Test
    fun `anime gets updated with seriesRepository anime series`() {
        val mockSeriesRepo = mockk<SeriesRepository> {
            every { getSeries() } returns flowOf(listOf(createSeriesDomain(seriesType = SeriesType.Anime)))
        }
        val mockUserRepo = mockk<UserRepository> {
            every { user } returns mockk()
        }
        val mockObserver = mockk<Observer<SeriesProgress>> {
            every { onChanged(any()) } just Runs
        }

        val classUnderTest = ProfileViewModel(mockSeriesRepo, mockUserRepo)
        classUnderTest.anime.observeForever(mockObserver)

        assertNotNull(classUnderTest.anime.value)
    }

    @Test
    fun `manga gets updated with seriesRepository manga series`() {
        val mockSeriesRepo = mockk<SeriesRepository> {
            every { getSeries() } returns flowOf(listOf(createSeriesDomain(seriesType = SeriesType.Manga)))
        }
        val mockUserRepo = mockk<UserRepository> {
            every { user } returns mockk()
        }
        val mockObserver = mockk<Observer<SeriesProgress>> {
            every { onChanged(any()) } just Runs
        }

        val classUnderTest = ProfileViewModel(mockSeriesRepo, mockUserRepo)
        classUnderTest.manga.observeForever(mockObserver)

        assertNotNull(classUnderTest.manga.value)
    }

    @Test
    fun `series has expected total items`() {
        val mockSeriesRepo = mockk<SeriesRepository> {
            every { getSeries() } returns flowOf(
                listOf(
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.Planned),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.Planned),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.Dropped),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.Dropped)
                )
            )
        }
        val mockUserRepo = mockk<UserRepository> {
            every { user } returns mockk()
        }
        val mockObserver = mockk<Observer<SeriesProgress>> {
            every { onChanged(any()) } just Runs
        }

        val classUnderTest = ProfileViewModel(mockSeriesRepo, mockUserRepo)
        classUnderTest.anime.observeForever(mockObserver)

        assertEquals("4", classUnderTest.anime.value?.total)
    }

    @Test
    fun `series has expected current items`() {
        val mockSeriesRepo = mockk<SeriesRepository> {
            every { getSeries() } returns flowOf(
                listOf(
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.Current),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.Planned),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.Dropped),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.Dropped)
                )
            )
        }
        val mockUserRepo = mockk<UserRepository> {
            every { user } returns mockk()
        }
        val mockObserver = mockk<Observer<SeriesProgress>> {
            every { onChanged(any()) } just Runs
        }

        val classUnderTest = ProfileViewModel(mockSeriesRepo, mockUserRepo)
        classUnderTest.anime.observeForever(mockObserver)

        assertEquals("1", classUnderTest.anime.value?.current)
    }

    @Test
    fun `series has expected completed items`() {
        val mockSeriesRepo = mockk<SeriesRepository> {
            every { getSeries() } returns flowOf(
                listOf(
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.Completed),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.Completed),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.Dropped),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.Dropped)
                )
            )
        }
        val mockUserRepo = mockk<UserRepository> {
            every { user } returns mockk()
        }
        val mockObserver = mockk<Observer<SeriesProgress>> {
            every { onChanged(any()) } just Runs
        }

        val classUnderTest = ProfileViewModel(mockSeriesRepo, mockUserRepo)
        classUnderTest.anime.observeForever(mockObserver)

        assertEquals("2", classUnderTest.anime.value?.completed)
    }

    @Test
    fun `series has expected onHold items`() {
        val mockSeriesRepo = mockk<SeriesRepository> {
            every { getSeries() } returns flowOf(
                listOf(
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.OnHold)
                )
            )
        }
        val mockUserRepo = mockk<UserRepository> {
            every { user } returns mockk()
        }
        val mockObserver = mockk<Observer<SeriesProgress>> {
            every { onChanged(any()) } just Runs
        }

        val classUnderTest = ProfileViewModel(mockSeriesRepo, mockUserRepo)
        classUnderTest.anime.observeForever(mockObserver)

        assertEquals("4", classUnderTest.anime.value?.onHold)
    }

    @Test
    fun `series has expected dropped items`() {
        val mockSeriesRepo = mockk<SeriesRepository> {
            every { getSeries() } returns flowOf(
                listOf(
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.OnHold)
                )
            )
        }
        val mockUserRepo = mockk<UserRepository> {
            every { user } returns mockk()
        }
        val mockObserver = mockk<Observer<SeriesProgress>> {
            every { onChanged(any()) } just Runs
        }

        val classUnderTest = ProfileViewModel(mockSeriesRepo, mockUserRepo)
        classUnderTest.anime.observeForever(mockObserver)

        assertEquals("0", classUnderTest.anime.value?.dropped)
    }

    @Test
    fun `series has expected planned items`() {
        val mockSeriesRepo = mockk<SeriesRepository> {
            every { getSeries() } returns flowOf(
                listOf(
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.Planned)
                )
            )
        }
        val mockUserRepo = mockk<UserRepository> {
            every { user } returns mockk()
        }
        val mockObserver = mockk<Observer<SeriesProgress>> {
            every { onChanged(any()) } just Runs
        }

        val classUnderTest = ProfileViewModel(mockSeriesRepo, mockUserRepo)
        classUnderTest.anime.observeForever(mockObserver)

        assertEquals("1", classUnderTest.anime.value?.planned)
    }

    @Test
    fun `series has expected unknown items`() {
        val mockSeriesRepo = mockk<SeriesRepository> {
            every { getSeries() } returns flowOf(
                listOf(
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.Unknown),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesDomain(userSeriesStatus = UserSeriesStatus.Unknown)
                )
            )
        }
        val mockUserRepo = mockk<UserRepository> {
            every { user } returns mockk()
        }
        val mockObserver = mockk<Observer<SeriesProgress>> {
            every { onChanged(any()) } just Runs
        }

        val classUnderTest = ProfileViewModel(mockSeriesRepo, mockUserRepo)
        classUnderTest.anime.observeForever(mockObserver)

        assertEquals("2", classUnderTest.anime.value?.unknown)
    }
}
