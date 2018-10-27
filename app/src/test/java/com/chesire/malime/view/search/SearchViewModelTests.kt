package com.chesire.malime.view.search

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.chesire.malime.R
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.repositories.Library
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.internal.verification.Times

class SearchViewModelTests {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var testObject: SearchViewModel
    private val searchApi = mock<SearchApi> { }
    private val library = mock<Library> { }
    private val seriesObserver = mock<Observer<List<MalimeModel>>> { }
    private val searchObserver = mock<Observer<List<MalimeModel>>> { }
    private val testScheduler = TestScheduler()

    @Before
    fun setup() {
        val mockItems = mock<Observable<List<MalimeModel>>> { }
        val flowableItems = mock<Flowable<List<MalimeModel>>> { }
        `when`(mockItems.toFlowable(BackpressureStrategy.ERROR)).thenReturn(flowableItems)
        `when`(library.observeLibrary()).thenReturn(mockItems)

        testObject = SearchViewModel(searchApi, library)
            .apply {
                observeScheduler = testScheduler
                subscribeScheduler = testScheduler
                series.observeForever(seriesObserver)
                searchItems.observeForever(searchObserver)
            }
    }

    @After
    fun teardown() {
        testObject.series.removeObserver(seriesObserver)
        testObject.searchItems.removeObserver(searchObserver)
    }

    @Test
    fun `cannot search for series with empty search text`() {
        testObject.params.searchText = ""

        testObject.searchForSeries(ItemType.Anime) { }

        verify(searchApi, Times(0)).searchForSeriesWith(
            "", ItemType.Anime
        )
    }

    @Test
    fun `cannot search for series with unknown item type`() {
        // this branch should never be able to be hit in production code
        val searchText = "Some series"
        testObject.params.searchText = searchText

        testObject.searchForSeries(ItemType.Unknown) { }

        verify(searchApi, Times(0)).searchForSeriesWith(
            "Some Series", ItemType.Unknown
        )
    }

    @Test
    fun `at the end of search, the searching flag is reset`() {
        testObject.params.searchText = "Some series"

        `when`(
            searchApi.searchForSeriesWith(testObject.params.searchText, ItemType.Anime)
        ).thenReturn(
            Single.error(Exception("Test Exception"))
        )

        testObject.searchForSeries(ItemType.Anime) { }

        assertTrue(testObject.params.searching)
        testScheduler.triggerActions()
        assertFalse(testObject.params.searching)
    }

    @Test
    fun `on failed search error message is displayed`() {
        var actualResource = 0
        testObject.params.searchText = "Some series"

        `when`(
            searchApi.searchForSeriesWith(testObject.params.searchText, ItemType.Anime)
        ).thenReturn(
            Single.error(Exception("Test Exception"))
        )

        testObject.searchForSeries(ItemType.Anime) { actualResource = it }
        testScheduler.triggerActions()

        assertEquals(R.string.search_failed_general_error, actualResource)
    }

    @Test
    fun `on successful search, with no items error callback is invoked`() {
        val expectedList = listOf<MalimeModel>()
        var actualResource = 0
        testObject.params.searchText = "Some series"

        `when`(
            searchApi.searchForSeriesWith(testObject.params.searchText, ItemType.Anime)
        ).thenReturn(
            Single.just(expectedList)
        )

        testObject.searchForSeries(ItemType.Anime) { actualResource = it }
        testScheduler.triggerActions()

        assertEquals(R.string.search_failed_no_items, actualResource)
    }

    @Test
    fun `on successful search, the searchItems are updated`() {
        val expectedList = listOf<MalimeModel>()
        testObject.params.searchText = "Some series"

        `when`(
            searchApi.searchForSeriesWith(testObject.params.searchText, ItemType.Anime)
        ).thenReturn(
            Single.just(expectedList)
        )

        testObject.searchForSeries(ItemType.Anime) { }
        testScheduler.triggerActions()

        verify(searchObserver).onChanged(expectedList)
    }

    @Test
    fun `on successful search, no error callback is invoked`() {
        val expectedList = listOf<MalimeModel>(mock { })
        var invoked = false
        testObject.params.searchText = "Some series"

        `when`(
            searchApi.searchForSeriesWith(testObject.params.searchText, ItemType.Anime)
        ).thenReturn(
            Single.just(expectedList)
        )

        testObject.searchForSeries(ItemType.Anime) { invoked = true }
        testScheduler.triggerActions()

        verify(searchObserver).onChanged(expectedList)
        assertFalse(invoked)
    }

    @Test
    fun `add new series fires callback on failure`() {
        val malimeModel = mock<MalimeModel> { }
        var callbackResult = false

        `when`(
            library.sendNewToApi(malimeModel)
        ).thenReturn(
            Single.error(Exception("Test Exception"))
        )

        testObject.addNewSeries(malimeModel) {
            callbackResult = false
        }
        testScheduler.triggerActions()

        assertFalse(callbackResult)
    }

    @Test
    fun `add new series fires callback on success`() {
        val malimeModel = mock<MalimeModel> { }
        val returnedModel = mock<MalimeModel> { }
        var callbackResult = false

        `when`(
            library.sendNewToApi(malimeModel)
        ).thenReturn(
            Single.just(returnedModel)
        )

        testObject.addNewSeries(malimeModel) {
            callbackResult = true
        }
        testScheduler.triggerActions()

        assertTrue(callbackResult)
    }

    @Test
    fun `add new series updates local library on success`() {
        val malimeModel = mock<MalimeModel> { }
        val returnedModel = mock<MalimeModel> { }

        `when`(
            library.sendNewToApi(malimeModel)
        ).thenReturn(
            Single.just(returnedModel)
        )

        testObject.addNewSeries(malimeModel) {
            // No callback checked here
        }
        testScheduler.triggerActions()

        verify(library).insertIntoLocalLibrary(returnedModel)
    }
}
