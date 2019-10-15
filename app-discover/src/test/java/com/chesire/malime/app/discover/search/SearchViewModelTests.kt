package com.chesire.malime.app.discover.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.malime.core.AuthCaster
import com.chesire.malime.core.flags.AsyncState
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.series.SeriesRepository
import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.SearchApi
import com.chesire.malime.testing.CoroutinesMainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SearchViewModelTests {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineRule = CoroutinesMainDispatcherRule()

    @Test
    fun `performSearch with no search title posts error`() {
        val mockRepo = mockk<SeriesRepository>()
        val mockSearch = mockk<SearchApi>()
        val mockObserver = mockk<Observer<AsyncState<List<SeriesModel>, SearchError>>> {
            every { onChanged(any()) } just Runs
        }
        val mockAuthCaster = mockk<AuthCaster>()

        SearchViewModel(mockRepo, mockSearch, mockAuthCaster).run {
            searchResults.observeForever(mockObserver)
            performSearch()

            assertEquals(SearchError.MissingTitle, (searchResults.value as AsyncState.Error).error)
        }
    }

    @Test
    fun `performSearch with seriesType of anime uses correct api`() {
        val mockRepo = mockk<SeriesRepository>()
        val mockSearch = mockk<SearchApi> {
            coEvery { searchForAnime(any()) } returns Resource.Error("", 0)
        }
        val mockObserver = mockk<Observer<AsyncState<List<SeriesModel>, SearchError>>> {
            every { onChanged(any()) } just Runs
        }
        val mockAuthCaster = mockk<AuthCaster>()

        SearchViewModel(mockRepo, mockSearch, mockAuthCaster).run {
            searchResults.observeForever(mockObserver)
            searchTitle.value = "Test"
            performSearch()

            coVerify { mockSearch.searchForAnime("Test") }
        }
    }

    @Test
    fun `performSearch with seriesType of manga uses correct api`() {
        val mockRepo = mockk<SeriesRepository>()
        val mockSearch = mockk<SearchApi> {
            coEvery { searchForManga(any()) } returns Resource.Error("", 0)
        }
        val mockObserver = mockk<Observer<AsyncState<List<SeriesModel>, SearchError>>> {
            every { onChanged(any()) } just Runs
        }
        val mockAuthCaster = mockk<AuthCaster>()

        SearchViewModel(mockRepo, mockSearch, mockAuthCaster).run {
            searchResults.observeForever(mockObserver)
            searchTitle.value = "Test"
            seriesType = SeriesType.Manga
            performSearch()

            coVerify { mockSearch.searchForManga("Test") }
        }
    }

    @Test
    fun `performSearch on success posts success with data`() {
        val expected = listOf<SeriesModel>(mockk())
        val mockRepo = mockk<SeriesRepository>()
        val mockSearch = mockk<SearchApi> {
            coEvery {
                searchForAnime(any())
            } coAnswers {
                Resource.Success(expected)
            }
        }
        val mockObserver = mockk<Observer<AsyncState<List<SeriesModel>, SearchError>>> {
            every { onChanged(any()) } just Runs
        }
        val mockAuthCaster = mockk<AuthCaster>()

        SearchViewModel(mockRepo, mockSearch, mockAuthCaster).run {
            searchResults.observeForever(mockObserver)
            searchTitle.value = "Test"
            seriesType = SeriesType.Anime
            performSearch()

            assertEquals(expected, (searchResults.value as AsyncState.Success).data)
        }
    }
}
