package com.chesire.malime.view.maldisplay

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.repositories.Library
import com.nhaarman.mockitokotlin2.mock
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

class MalDisplayViewModelTests {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var testObject: MalDisplayViewModel
    private val library = mock<Library> { }
    private val seriesObserver = mock<Observer<List<MalimeModel>>> { }
    private val updateObserver = mock<Observer<UpdatingSeriesStatus>> { }
    private val testScheduler = TestScheduler()

    @Before
    fun setup() {
        val mockItems = mock<Observable<List<MalimeModel>>> { }
        val flowableItems = mock<Flowable<List<MalimeModel>>> { }
        `when`(mockItems.toFlowable(BackpressureStrategy.ERROR)).thenReturn(flowableItems)
        `when`(library.observeLibrary()).thenReturn(mockItems)

        testObject = MalDisplayViewModel(library)
            .apply {
                observeScheduler = testScheduler
                subscribeScheduler = testScheduler
                series.observeForever(seriesObserver)
                updateAllStatus.observeForever(updateObserver)
            }
    }

    @After
    fun teardown() {
        testObject.series.removeObserver(seriesObserver)
        testObject.updateAllStatus.removeObserver(updateObserver)
    }

    @Test
    fun `check for latest series progresses to error state on failure`() {
        `when`(
            library.updateLibraryFromApi()
        ).thenReturn(
            Observable.error(Exception("Test Exception"))
        )

        testObject.checkForLatestSeries()
        testScheduler.triggerActions()

        verify(updateObserver).onChanged(UpdatingSeriesStatus.Updating)
        verify(updateObserver).onChanged(UpdatingSeriesStatus.Error)
    }

    @Test
    fun `check for latest series progresses to success state on success`() {
        val newItems = listOf<MalimeModel>()
        `when`(
            library.updateLibraryFromApi()
        ).thenReturn(
            Observable.just(newItems)
        )

        testObject.checkForLatestSeries()
        testScheduler.triggerActions()

        verify(updateObserver).onChanged(UpdatingSeriesStatus.Updating)
        verify(updateObserver).onChanged(UpdatingSeriesStatus.Finished)
    }

    @Test
    fun `check for latest series updates local library on success`() {
        val newItems = listOf<MalimeModel>()
        `when`(
            library.updateLibraryFromApi()
        ).thenReturn(
            Observable.just(newItems)
        )

        testObject.checkForLatestSeries()
        testScheduler.triggerActions()

        verify(library).insertIntoLocalLibrary(newItems)
    }

    @Test
    fun `update series fires callback on failure`() {
        val malimeModel = mock<MalimeModel> { }
        var callbackResult = false

        `when`(
            library.sendUpdateToApi(malimeModel, 5, UserSeriesStatus.Completed)
        ).thenReturn(
            Single.error(Exception("Test Exception"))
        )

        testObject.updateSeries(malimeModel, 5, UserSeriesStatus.Completed) {
            callbackResult = false
        }
        testScheduler.triggerActions()

        assertFalse(callbackResult)
    }

    @Test
    fun `update series fires callback on success`() {
        val malimeModel = mock<MalimeModel> { }
        val returnedModel = mock<MalimeModel> { }
        var callbackResult = false

        `when`(
            library.sendUpdateToApi(malimeModel, 5, UserSeriesStatus.Completed)
        ).thenReturn(
            Single.just(returnedModel)
        )

        testObject.updateSeries(malimeModel, 5, UserSeriesStatus.Completed) {
            callbackResult = true
        }
        testScheduler.triggerActions()

        assertTrue(callbackResult)
    }

    @Test
    fun `update series updates local library on success`() {
        val malimeModel = mock<MalimeModel> { }
        val returnedModel = mock<MalimeModel> { }

        `when`(
            library.sendUpdateToApi(malimeModel, 5, UserSeriesStatus.Completed)
        ).thenReturn(
            Single.just(returnedModel)
        )

        testObject.updateSeries(malimeModel, 5, UserSeriesStatus.Completed) {
            // No callback checked here
        }
        testScheduler.triggerActions()

        verify(library).updateInLocalLibrary(returnedModel)
    }

    @Test
    fun `delete series fires callback on failure`() {
        val malimeModel = mock<MalimeModel> { }
        var callbackResult = false

        `when`(library.sendDeleteToApi(malimeModel))
            .thenReturn(Single.error(Exception("Test Exception")))

        testObject.deleteSeries(malimeModel) { callbackResult = false }
        testScheduler.triggerActions()

        assertFalse(callbackResult)
    }

    @Test
    fun `delete series fires callback on success`() {
        val malimeModel = mock<MalimeModel> { }
        val returnedModel = mock<MalimeModel> { }
        var callbackResult = false

        `when`(library.sendDeleteToApi(malimeModel))
            .thenReturn(Single.just(returnedModel))

        testObject.deleteSeries(malimeModel) { callbackResult = true }
        testScheduler.triggerActions()

        assertTrue(callbackResult)
    }

    @Test
    fun `delete series fires updates library success`() {
        val malimeModel = mock<MalimeModel> { }
        val returnedModel = mock<MalimeModel> { }

        `when`(library.sendDeleteToApi(malimeModel))
            .thenReturn(Single.just(returnedModel))

        testObject.deleteSeries(malimeModel) {
            // No callback checked here
        }
        testScheduler.triggerActions()

        verify(library).deleteFromLocalLibrary(returnedModel)
    }
}
