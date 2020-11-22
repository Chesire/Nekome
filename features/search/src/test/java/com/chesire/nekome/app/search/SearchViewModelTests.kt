package com.chesire.nekome.app.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.nekome.core.AuthCaster
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.search.api.SearchApi
import com.chesire.nekome.testing.CoroutinesMainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SearchViewModelTests {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutinesMainDispatcherRule()

    @Test
    fun `executeSearch with empty title posts error`() {
        val observerSlot = slot<AsyncState<List<SeriesModel>, SearchError>>()
        val mockSearch = mockk<SearchApi>()
        val mockCaster = mockk<AuthCaster>()
        val mockObserver = mockk<Observer<AsyncState<List<SeriesModel>, SearchError>>> {
            every { onChanged(capture(observerSlot)) } just Runs
        }
        val testObject = SearchViewModel(mockSearch, mockCaster)

        testObject.searchResult.observeForever(mockObserver)
        testObject.executeSearch(SearchData("", SeriesType.Anime))

        assertTrue(observerSlot.isCaptured)
        assertTrue(observerSlot.captured is AsyncState.Error)
    }

    @Test
    fun `executeSearch with unknown seriesType posts error`() {
        val observerSlot = slot<AsyncState<List<SeriesModel>, SearchError>>()
        val mockSearch = mockk<SearchApi>()
        val mockCaster = mockk<AuthCaster>()
        val mockObserver = mockk<Observer<AsyncState<List<SeriesModel>, SearchError>>> {
            every { onChanged(capture(observerSlot)) } just Runs
        }
        val testObject = SearchViewModel(mockSearch, mockCaster)

        testObject.searchResult.observeForever(mockObserver)
        testObject.executeSearch(SearchData("New Title", SeriesType.Unknown))

        assertTrue(observerSlot.isCaptured)
        assertTrue(observerSlot.captured is AsyncState.Error)
    }

    @Test
    fun `executeSearch with anime seriesType executes searchForAnime`() {
        val mockSearch = mockk<SearchApi> {
            coEvery {
                searchForAnime(any())
            } coAnswers {
                Resource.Error("")
            }
        }
        val mockCaster = mockk<AuthCaster>()
        val testObject = SearchViewModel(mockSearch, mockCaster)

        testObject.executeSearch(SearchData("title", SeriesType.Anime))

        coVerify { mockSearch.searchForAnime(any()) }
    }

    @Test
    fun `executeSearch with manga seriesType executes searchForManga`() {
        val mockSearch = mockk<SearchApi> {
            coEvery {
                searchForManga(any())
            } coAnswers {
                Resource.Error("")
            }
        }
        val mockCaster = mockk<AuthCaster>()
        val testObject = SearchViewModel(mockSearch, mockCaster)

        testObject.executeSearch(SearchData("title", SeriesType.Manga))

        coVerify { mockSearch.searchForManga(any()) }
    }

    @Test
    fun `executeSearch on success but no data posts error`() {
        val observerSlot = slot<AsyncState<List<SeriesModel>, SearchError>>()
        val mockObserver = mockk<Observer<AsyncState<List<SeriesModel>, SearchError>>> {
            every { onChanged(capture(observerSlot)) } just Runs
        }
        val mockSearch = mockk<SearchApi> {
            coEvery {
                searchForAnime(any())
            } coAnswers {
                Resource.Success(emptyList())
            }
        }
        val mockCaster = mockk<AuthCaster>()
        val testObject = SearchViewModel(mockSearch, mockCaster)

        testObject.searchResult.observeForever(mockObserver)
        testObject.executeSearch(SearchData("title", SeriesType.Anime))

        coVerify { mockSearch.searchForAnime(any()) }
        assertTrue(observerSlot.isCaptured)
        assertTrue(observerSlot.captured is AsyncState.Error)
    }

    @Test
    fun `executeSearch on success posts the data`() {
        val observerSlot = slot<AsyncState<List<SeriesModel>, SearchError>>()
        val mockObserver = mockk<Observer<AsyncState<List<SeriesModel>, SearchError>>> {
            every { onChanged(capture(observerSlot)) } just Runs
        }
        val mockSearch = mockk<SearchApi> {
            coEvery {
                searchForAnime(any())
            } coAnswers {
                Resource.Success(listOf(mockk()))
            }
        }
        val mockCaster = mockk<AuthCaster>()
        val testObject = SearchViewModel(mockSearch, mockCaster)

        testObject.searchResult.observeForever(mockObserver)
        testObject.executeSearch(SearchData("title", SeriesType.Anime))

        coVerify { mockSearch.searchForAnime(any()) }
        assertTrue(observerSlot.isCaptured)
        assertTrue(observerSlot.captured is AsyncState.Success)
    }

    @Test
    fun `executeSearch on failure posts error`() {
        val observerSlot = slot<AsyncState<List<SeriesModel>, SearchError>>()
        val mockObserver = mockk<Observer<AsyncState<List<SeriesModel>, SearchError>>> {
            every { onChanged(capture(observerSlot)) } just Runs
        }
        val mockSearch = mockk<SearchApi> {
            coEvery {
                searchForAnime(any())
            } coAnswers {
                Resource.Error("")
            }
        }
        val mockCaster = mockk<AuthCaster>()
        val testObject = SearchViewModel(mockSearch, mockCaster)

        testObject.searchResult.observeForever(mockObserver)
        testObject.executeSearch(SearchData("title", SeriesType.Anime))

        coVerify { mockSearch.searchForAnime(any()) }
        assertTrue(observerSlot.isCaptured)
        assertTrue(observerSlot.captured is AsyncState.Error)
    }

    @Test
    fun `executeSearch on CouldNotRefresh failure posts to AuthCaster`() {
        val observerSlot = slot<AsyncState<List<SeriesModel>, SearchError>>()
        val mockObserver = mockk<Observer<AsyncState<List<SeriesModel>, SearchError>>> {
            every { onChanged(capture(observerSlot)) } just Runs
        }
        val mockSearch = mockk<SearchApi> {
            coEvery {
                searchForAnime(any())
            } coAnswers {
                Resource.Error("", Resource.Error.CouldNotRefresh)
            }
        }
        val mockCaster = mockk<AuthCaster> {
            every { issueRefreshingToken() } just Runs
        }
        val testObject = SearchViewModel(mockSearch, mockCaster)

        testObject.searchResult.observeForever(mockObserver)
        testObject.executeSearch(SearchData("title", SeriesType.Anime))

        verify { mockCaster.issueRefreshingToken() }
    }
}
