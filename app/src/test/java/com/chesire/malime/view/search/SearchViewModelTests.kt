package com.chesire.malime.view.search

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.test.mock.MockApplication
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.repositories.Library
import com.chesire.malime.customMock
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

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
        testObject = SearchViewModel(
            MockApplication(),
            searchApi,
            library,
            testScheduler,
            testScheduler
        )
        testObject.series.observeForever(seriesObserver)
        testObject.searchItems.observeForever(searchObserver)
    }

    @After
    fun teardown() {
        testObject.series.removeObserver(seriesObserver)
        testObject.searchItems.removeObserver(searchObserver)
    }

    @Test
    fun `cannot search for series with empty search text`() {

    }

    @Test
    fun `cannot search for series with unknown item type`() {
        // this branch should never be able to be hit
    }

    @Test
    fun `at the end of search, the searching flag is reset`() {

    }

    @Test
    fun `on successful search, the searchItems are updated`() {

    }

    @Test
    fun `add new series fires callback on failure`() {

    }

    @Test
    fun `add new series fires callback on success`() {

    }

    @Test
    fun `add new series updates local library on success`() {

    }
}