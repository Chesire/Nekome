@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.app.series.SeriesPreferences
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.testing.createSeriesDomain
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FilterSeriesUseCaseTest {

    private val pref = mockk<SeriesPreferences>()
    private lateinit var filterSeries: FilterSeriesUseCase

    @Before
    fun setup() {
        clearAllMocks()

        filterSeries = FilterSeriesUseCase(pref)
    }

    @Test
    fun `When invoking, then filtered list of series domain are returned`() = runTest {
        every { pref.filter } returns flowOf(
            mapOf(
                -1 to false,
                0 to true,
                1 to false,
                2 to false,
                3 to false,
                4 to false
            )
        )
        val input = listOf(
            createSeriesDomain(userSeriesStatus = UserSeriesStatus.Current),
            createSeriesDomain(userSeriesStatus = UserSeriesStatus.Completed),
            createSeriesDomain(userSeriesStatus = UserSeriesStatus.OnHold),
            createSeriesDomain(userSeriesStatus = UserSeriesStatus.Dropped),
            createSeriesDomain(userSeriesStatus = UserSeriesStatus.Planned),
            createSeriesDomain(
                seriesType = SeriesType.Manga,
                userSeriesStatus = UserSeriesStatus.Current
            )
        )

        val result = filterSeries(input, SeriesType.Anime)

        assertEquals(1, result.first().count())
        assertEquals(UserSeriesStatus.Current, result.first().first().userSeriesStatus)
    }
}
