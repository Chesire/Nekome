package com.chesire.nekome.app.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.datasource.search.SearchDomain
import com.chesire.nekome.datasource.search.remote.SearchApi
import com.chesire.nekome.testing.CoroutinesMainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SearchViewModelTests {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutinesMainDispatcherRule()

    private val map = SearchDomainMapper()

    @Test
    fun `executeSearch with empty title posts error`() {
        val observerSlot = slot<SearchState>()
        val mockSearch = mockk<SearchApi>()
        val mockObserver = mockk<Observer<SearchState>> {
            every { onChanged(capture(observerSlot)) } just Runs
        }
        val testObject = SearchViewModel(mockSearch, map)

        testObject.searchState.observeForever(mockObserver)
        testObject.executeSearch(SearchData("", SeriesType.Anime))

        assertTrue(observerSlot.isCaptured)
        assertTrue(observerSlot.captured is SearchState.EmptyTitle)
    }

    @Test
    fun `executeSearch with unknown seriesType posts error`() {
        val observerSlot = slot<SearchState>()
        val mockSearch = mockk<SearchApi>()
        val mockObserver = mockk<Observer<SearchState>> {
            every { onChanged(capture(observerSlot)) } just Runs
        }
        val testObject = SearchViewModel(mockSearch, map)

        testObject.searchState.observeForever(mockObserver)
        testObject.executeSearch(SearchData("New Title", SeriesType.Unknown))

        assertTrue(observerSlot.isCaptured)
        assertTrue(observerSlot.captured is SearchState.NoTypeSelected)
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
        val testObject = SearchViewModel(mockSearch, map)

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
        val testObject = SearchViewModel(mockSearch, map)

        testObject.executeSearch(SearchData("title", SeriesType.Manga))

        coVerify { mockSearch.searchForManga(any()) }
    }

    @Test
    fun `executeSearch on success but no data posts error`() {
        val observerSlot = slot<SearchState>()
        val mockObserver = mockk<Observer<SearchState>> {
            every { onChanged(capture(observerSlot)) } just Runs
        }
        val mockSearch = mockk<SearchApi> {
            coEvery {
                searchForAnime(any())
            } coAnswers {
                Resource.Success(emptyList())
            }
        }
        val testObject = SearchViewModel(mockSearch, map)

        testObject.searchState.observeForever(mockObserver)
        testObject.executeSearch(SearchData("title", SeriesType.Anime))

        coVerify { mockSearch.searchForAnime(any()) }
        assertTrue(observerSlot.isCaptured)
        assertTrue(observerSlot.captured is SearchState.NoSeriesFound)
    }

    @Test
    fun `executeSearch on success posts the data`() {
        val expected = "title"
        val observerSlot = slot<SearchState>()
        val mockObserver = mockk<Observer<SearchState>> {
            every { onChanged(capture(observerSlot)) } just Runs
        }
        val mockSearch = mockk<SearchApi> {
            coEvery {
                searchForAnime(any())
            } coAnswers {
                Resource.Success(listOf(createSearchDomain()))
            }
        }
        val testObject = SearchViewModel(mockSearch, map)

        testObject.searchState.observeForever(mockObserver)
        testObject.executeSearch(SearchData(expected, SeriesType.Anime))

        val captured = observerSlot.captured
        coVerify { mockSearch.searchForAnime(any()) }
        assertTrue(observerSlot.isCaptured)
        check(captured is SearchState.Success)
        assertEquals(expected, captured.searchTerm)
    }

    @Test
    fun `executeSearch on failure posts error`() {
        val observerSlot = slot<SearchState>()
        val mockObserver = mockk<Observer<SearchState>> {
            every { onChanged(capture(observerSlot)) } just Runs
        }
        val mockSearch = mockk<SearchApi> {
            coEvery {
                searchForAnime(any())
            } coAnswers {
                Resource.Error("")
            }
        }
        val testObject = SearchViewModel(mockSearch, map)

        testObject.searchState.observeForever(mockObserver)
        testObject.executeSearch(SearchData("title", SeriesType.Anime))

        coVerify { mockSearch.searchForAnime(any()) }
        assertTrue(observerSlot.isCaptured)
        assertTrue(observerSlot.captured is SearchState.GenericError)
    }

    private fun createSearchDomain() =
        SearchDomain(
            0,
            SeriesType.Anime,
            "synopsis",
            "canonicalTitle",
            Subtype.Unknown,
            ImageModel.empty
        )
}
