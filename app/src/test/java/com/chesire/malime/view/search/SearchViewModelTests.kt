package com.chesire.malime.view.search

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.test.mock.MockApplication
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.repositories.Library
import com.chesire.malime.customMock
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.internal.verification.Times

@Suppress("DEPRECATION")
class SearchViewModelTests {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var testObject: SearchViewModel
    private val searchApi: SearchApi = customMock()
    private val library: Library = customMock()
    private val seriesObserver: Observer<List<MalimeModel>> = customMock()
    private val searchObserver: Observer<List<MalimeModel>> = customMock()
    private val testScheduler = TestScheduler()

    @Before
    fun setup() {
        val mockItems: Observable<List<MalimeModel>> = customMock()
        val flowableItems: Flowable<List<MalimeModel>> = customMock()
        `when`(mockItems.toFlowable(BackpressureStrategy.ERROR)).thenReturn(flowableItems)
        `when`(library.observeLibrary()).thenReturn(mockItems)

        testObject = SearchViewModel(MockApplication(), searchApi, library)
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

        testObject.searchForSeries(ItemType.Anime)

        verify(searchApi, Times(0)).searchForSeriesWith(
            "", ItemType.Anime
        )
    }

    @Test
    fun `cannot search for series with unknown item type`() {
        // this branch should never be able to be hit in production code
        val searchText = "Some series"
        testObject.params.searchText = searchText

        testObject.searchForSeries(ItemType.Unknown)

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
            Observable.error(Exception("Test Exception"))
        )

        testObject.searchForSeries(ItemType.Anime)

        assertTrue(testObject.params.searching)
        testScheduler.triggerActions()
        assertFalse(testObject.params.searching)
    }

    @Test
    fun `on successful search, the searchItems are updated`() {
        val expectedList = listOf<MalimeModel>()
        testObject.params.searchText = "Some series"

        `when`(
            searchApi.searchForSeriesWith(testObject.params.searchText, ItemType.Anime)
        ).thenReturn(
            Observable.just(expectedList)
        )

        testObject.searchForSeries(ItemType.Anime)
        testScheduler.triggerActions()

        verify(searchObserver).onChanged(expectedList)
    }

    @Test
    fun `add new series fires callback on failure`() {
        val malimeModel: MalimeModel = customMock()
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
        val malimeModel: MalimeModel = customMock()
        val returnedModel: MalimeModel = customMock()
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
        val malimeModel: MalimeModel = customMock()
        val returnedModel: MalimeModel = customMock()

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