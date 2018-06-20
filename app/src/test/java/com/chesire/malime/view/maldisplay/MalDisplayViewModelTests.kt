package com.chesire.malime.view.maldisplay

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.test.mock.MockApplication
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
class MalDisplayViewModelTests {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var testObject: MalDisplayViewModel
    private val library: Library = customMock()
    private val seriesObserver: Observer<List<MalimeModel>> = customMock()
    private val updateObserver: Observer<UpdatingSeriesStatus> = customMock()
    private val testScheduler = TestScheduler()

    @Before
    fun setup() {
        testObject = MalDisplayViewModel(
            MockApplication(),
            library,
            testScheduler,
            testScheduler
        )
        testObject.series.observeForever(seriesObserver)
        testObject.updateAllStatus.observeForever(updateObserver)
    }

    @After
    fun teardown() {
        testObject.series.removeObserver(seriesObserver)
        testObject.updateAllStatus.removeObserver(updateObserver)
    }

    @Test
    fun `check for latest series progresses to error state on failure`() {

    }

    @Test
    fun `check for latest series progresses to success state on success`() {

    }

    @Test
    fun `check for latest series updates local library on success`() {

    }

    @Test
    fun `update series fires callback on failure`() {

    }

    @Test
    fun `update series fires callback on success`() {

    }

    @Test
    fun `update series updates local library on success`() {

    }
}