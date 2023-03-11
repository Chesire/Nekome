@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.app.series.SeriesPreferences
import com.chesire.nekome.core.flags.SortOption
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

class SortSeriesUseCaseTest {

    private val pref = mockk<SeriesPreferences>()
    private lateinit var sortSeries: SortSeriesUseCase

    @Before
    fun setup() {
        clearAllMocks()

        sortSeries = SortSeriesUseCase(pref)
    }

    @Test
    fun `When invoking, if SortOption is Default, Then sorted list is returned`() = runTest {
        every { pref.sort } returns flowOf(SortOption.Default)
        val defaultDomain = createSeriesDomain()
        val input = listOf(
            defaultDomain.copy(userId = 1),
            defaultDomain.copy(userId = 5),
            defaultDomain.copy(userId = 513),
            defaultDomain.copy(userId = 3),
            defaultDomain.copy(userId = 89),
            defaultDomain.copy(userId = 2)
        )
        val expected = listOf(
            defaultDomain.copy(userId = 1),
            defaultDomain.copy(userId = 2),
            defaultDomain.copy(userId = 3),
            defaultDomain.copy(userId = 5),
            defaultDomain.copy(userId = 89),
            defaultDomain.copy(userId = 513)
        )

        val result = sortSeries(input)

        assertEquals(expected, result.first())
    }

    @Test
    fun `When invoking, if SortOption is Title, Then sorted list is returned`() = runTest {
        every { pref.sort } returns flowOf(SortOption.Title)
        val defaultDomain = createSeriesDomain()
        val input = listOf(
            defaultDomain.copy(title = "Chainsaw Man"),
            defaultDomain.copy(title = "Naruto"),
            defaultDomain.copy(title = "Bleach"),
            defaultDomain.copy(title = "One Piece"),
            defaultDomain.copy(title = "Dragonball"),
            defaultDomain.copy(title = "Hellsing")
        )
        val expected = listOf(
            defaultDomain.copy(title = "Bleach"),
            defaultDomain.copy(title = "Chainsaw Man"),
            defaultDomain.copy(title = "Dragonball"),
            defaultDomain.copy(title = "Hellsing"),
            defaultDomain.copy(title = "Naruto"),
            defaultDomain.copy(title = "One Piece")
        )

        val result = sortSeries(input)

        assertEquals(expected, result.first())
    }

    @Test
    fun `When invoking, if SortOption is StartDate, Then sorted list is returned`() = runTest {
        every { pref.sort } returns flowOf(SortOption.StartDate)
        val defaultDomain = createSeriesDomain()
        val input = listOf(
            defaultDomain.copy(startDate = "23-01-12T00:00:00.000Z"),
            defaultDomain.copy(startDate = "23-01-15T00:00:00.000Z"),
            defaultDomain.copy(startDate = "23-01-10T00:00:00.000Z"),
            defaultDomain.copy(startDate = "23-02-12T00:00:00.000Z"),
            defaultDomain.copy(startDate = "22-01-12T00:00:00.000Z"),
            defaultDomain.copy(startDate = "22-04-30T00:00:00.000Z")
        )
        val expected = listOf(
            defaultDomain.copy(startDate = "22-01-12T00:00:00.000Z"),
            defaultDomain.copy(startDate = "22-04-30T00:00:00.000Z"),
            defaultDomain.copy(startDate = "23-01-10T00:00:00.000Z"),
            defaultDomain.copy(startDate = "23-01-12T00:00:00.000Z"),
            defaultDomain.copy(startDate = "23-01-15T00:00:00.000Z"),
            defaultDomain.copy(startDate = "23-02-12T00:00:00.000Z")
        )

        val result = sortSeries(input)

        assertEquals(expected, result.first())
    }

    @Test
    fun `When invoking, if SortOption is EndDate, Then sorted list is returned`() = runTest {
        every { pref.sort } returns flowOf(SortOption.EndDate)
        val defaultDomain = createSeriesDomain()
        val input = listOf(
            defaultDomain.copy(endDate = "23-01-12T00:00:00.000Z"),
            defaultDomain.copy(endDate = "23-01-15T00:00:00.000Z"),
            defaultDomain.copy(endDate = "23-01-10T00:00:00.000Z"),
            defaultDomain.copy(endDate = "23-02-12T00:00:00.000Z"),
            defaultDomain.copy(endDate = "22-01-12T00:00:00.000Z"),
            defaultDomain.copy(endDate = "22-04-30T00:00:00.000Z")
        )
        val expected = listOf(
            defaultDomain.copy(endDate = "22-01-12T00:00:00.000Z"),
            defaultDomain.copy(endDate = "22-04-30T00:00:00.000Z"),
            defaultDomain.copy(endDate = "23-01-10T00:00:00.000Z"),
            defaultDomain.copy(endDate = "23-01-12T00:00:00.000Z"),
            defaultDomain.copy(endDate = "23-01-15T00:00:00.000Z"),
            defaultDomain.copy(endDate = "23-02-12T00:00:00.000Z")
        )

        val result = sortSeries(input)

        assertEquals(expected, result.first())
    }

    @Test
    fun `When invoking, if SortOption is Rating, Then sorted list is returned`() = runTest {
        every { pref.sort } returns flowOf(SortOption.Rating)
        val defaultDomain = createSeriesDomain()
        val input = listOf(
            defaultDomain.copy(rating = 0),
            defaultDomain.copy(rating = 5),
            defaultDomain.copy(rating = 3),
            defaultDomain.copy(rating = 19),
            defaultDomain.copy(rating = 9),
            defaultDomain.copy(rating = 6)
        )
        val expected = listOf(
            defaultDomain.copy(rating = 0),
            defaultDomain.copy(rating = 3),
            defaultDomain.copy(rating = 5),
            defaultDomain.copy(rating = 6),
            defaultDomain.copy(rating = 9),
            defaultDomain.copy(rating = 19)
        )

        val result = sortSeries(input)

        assertEquals(expected, result.first())
    }
}
