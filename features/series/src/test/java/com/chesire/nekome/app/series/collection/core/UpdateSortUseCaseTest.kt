@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.core.flags.SortOption
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

class UpdateSortUseCaseTest {

    private val pref = mockk<SeriesPreferences>()
    private lateinit var updateSort: UpdateSortUseCase

    @Before
    fun setup() {
        clearAllMocks()

        updateSort = UpdateSortUseCase(pref)
    }

    @Test
    fun `When invoking, update sort with new option`() = runTest {
        coEvery { pref.updateSort(any()) } just runs

        pref.updateSort(SortOption.Rating)

        coEvery { pref.updateSort(SortOption.Rating) }
    }
}
