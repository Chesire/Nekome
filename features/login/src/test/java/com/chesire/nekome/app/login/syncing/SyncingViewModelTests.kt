package com.chesire.nekome.app.login.syncing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.library.SeriesRepository
import com.chesire.nekome.testing.CoroutinesMainDispatcherRule
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
        val mockUser = mockk<UserRepository> {
            every { user } returns mockk()
        }
        val mockObserver = mockk<Observer<AsyncState<Any, Any>>> {
            every { onChanged(any()) } just Runs
        }

        SyncingViewModel(mockRepo, mockUser)
            .run {
                syncStatus.observeForever(mockObserver)
                syncLatestData()
                assertTrue(syncStatus.value is AsyncState.Error)
            }
    }

    @Test
    fun `syncLatestData refreshManga failure posts error`() = runBlocking {
        val mockRepo = mockk<SeriesRepository> {
            coEvery { refreshAnime() } coAnswers { Resource.Success(mockk()) }
            coEvery { refreshManga() } coAnswers { Resource.Error("") }
        }
        val mockUser = mockk<UserRepository> {
            every { user } returns mockk()
        }
        val mockObserver = mockk<Observer<AsyncState<Any, Any>>> {
            every { onChanged(any()) } just Runs
        }

        SyncingViewModel(mockRepo, mockUser)
            .run {
                syncStatus.observeForever(mockObserver)
                syncLatestData()
                assertTrue(syncStatus.value is AsyncState.Error)
            }
    }

    @Test
    fun `syncLatestData all sync success posts success`() = runBlocking {
        val mockRepo = mockk<SeriesRepository> {
            coEvery { refreshAnime() } coAnswers { Resource.Success(mockk()) }
            coEvery { refreshManga() } coAnswers { Resource.Success(mockk()) }
        }
        val mockUser = mockk<UserRepository> {
            every { user } returns mockk()
        }
        val mockObserver = mockk<Observer<AsyncState<Any, Any>>> {
            every { onChanged(any()) } just Runs
        }

        SyncingViewModel(mockRepo, mockUser)
            .run {
                syncStatus.observeForever(mockObserver)
                syncLatestData()
                assertTrue(syncStatus.value is AsyncState.Success)
            }
    }
}
