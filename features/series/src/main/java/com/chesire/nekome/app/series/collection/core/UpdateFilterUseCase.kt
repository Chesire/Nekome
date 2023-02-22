package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.app.series.SeriesPreferences
import javax.inject.Inject

class UpdateFilterUseCase @Inject constructor(private val pref: SeriesPreferences) {

    suspend operator fun invoke(newFilter: Map<Int, Boolean>) = pref.updateFilter(newFilter)
}
