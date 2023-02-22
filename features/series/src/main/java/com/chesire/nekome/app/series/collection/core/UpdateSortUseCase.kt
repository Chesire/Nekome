package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.app.series.SeriesPreferences
import com.chesire.nekome.core.flags.SortOption
import javax.inject.Inject

class UpdateSortUseCase @Inject constructor(private val pref: SeriesPreferences) {

    suspend operator fun invoke(newSortOption: SortOption) = pref.updateSort(newSortOption)
}
