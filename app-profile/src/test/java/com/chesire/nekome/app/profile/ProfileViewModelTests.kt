package com.chesire.nekome.app.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.series.SeriesRepository
import com.chesire.nekome.testing.createSeriesModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTests {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `anime gets updated with seriesRepository anime series`() {
        val mockSeriesRepo = mockk<SeriesRepository> {
            every { series } returns MutableLiveData(listOf(createSeriesModel(seriesType = SeriesType.Anime)))
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
            every { series } returns MutableLiveData(listOf(createSeriesModel(seriesType = SeriesType.Manga)))
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
            every { series } returns MutableLiveData(
                listOf(
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.Planned),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.Planned),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.Dropped),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.Dropped)
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
            every { series } returns MutableLiveData(
                listOf(
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.Current),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.Planned),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.Dropped),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.Dropped)
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
            every { series } returns MutableLiveData(
                listOf(
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.Completed),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.Completed),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.Dropped),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.Dropped)
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
            every { series } returns MutableLiveData(
                listOf(
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.OnHold)
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
            every { series } returns MutableLiveData(
                listOf(
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.OnHold)
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
            every { series } returns MutableLiveData(
                listOf(
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.Planned)
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
            every { series } returns MutableLiveData(
                listOf(
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.Unknown),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.OnHold),
                    createSeriesModel(userSeriesStatus = UserSeriesStatus.Unknown)
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
