@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.core.preferences.SeriesPreferences
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateRateSeriesUseCaseTest {

    private val pref = mockk<SeriesPreferences>()
    private lateinit var updateRateSeries: UpdateRateSeriesUseCase

    @Before
    fun setup() {
        clearAllMocks()

        updateRateSeries = UpdateRateSeriesUseCase(pref)
    }

    @Test
    fun `When invoking, update pref with new value`() = runTest {
        coEvery { pref.updateRateSeriesOnCompletion(any()) } just runs

        updateRateSeries(true)

        coEvery { pref.updateRateSeriesOnCompletion(true) }
    }
}
