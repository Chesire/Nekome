package com.chesire.malime.flow.login.syncing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.malime.core.flags.AsyncState
import com.chesire.malime.CoroutinesMainDispatcherRule
import com.chesire.malime.server.Resource
import com.chesire.malime.repo.SeriesRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SyncingViewModelTests {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineRule = CoroutinesMainDispatcherRule()

    @Test
    fun `syncLatestData refreshAnime failure posts error`() = runBlocking {
        val mockRepo = mockk<SeriesRepository> {
            coEvery { refreshAnime() } coAnswers { Resource.Error("") }
            coEvery { refreshManga() } coAnswers { Resource.Success(mockk()) }
        }
        val mockObserver = mockk<Observer<com.chesire.malime.core.flags.AsyncState<Any, Any>>> {
            every { onChanged(any()) } just Runs
        }

        SyncingViewModel(mockRepo).run {
            syncStatus.observeForever(mockObserver)
            syncLatestData()
            assertTrue(syncStatus.value is com.chesire.malime.core.flags.AsyncState.Error)
        }
    }

    @Test
    fun `syncLatestData refreshManga failure posts error`() = runBlocking {
        val mockRepo = mockk<SeriesRepository> {
            coEvery { refreshAnime() } coAnswers { Resource.Success(mockk()) }
            coEvery { refreshManga() } coAnswers { Resource.Error("") }
        }
        val mockObserver = mockk<Observer<com.chesire.malime.core.flags.AsyncState<Any, Any>>> {
            every { onChanged(any()) } just Runs
        }

        SyncingViewModel(mockRepo).run {
            syncStatus.observeForever(mockObserver)
            syncLatestData()
            assertTrue(syncStatus.value is com.chesire.malime.core.flags.AsyncState.Error)
        }
    }

    @Test
    fun `syncLatestData all sync success posts success`() = runBlocking {
        val mockRepo = mockk<SeriesRepository> {
            coEvery { refreshAnime() } coAnswers { Resource.Success(mockk()) }
            coEvery { refreshManga() } coAnswers { Resource.Success(mockk()) }
        }
        val mockObserver = mockk<Observer<com.chesire.malime.core.flags.AsyncState<Any, Any>>> {
            every { onChanged(any()) } just Runs
        }

        SyncingViewModel(mockRepo).run {
            syncStatus.observeForever(mockObserver)
            syncLatestData()
            assertTrue(syncStatus.value is com.chesire.malime.core.flags.AsyncState.Success)
        }
    }
}
