package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.core.flags.SortOption
import com.chesire.nekome.core.preferences.SeriesPreferences
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class CurrentSortUseCase @Inject constructor(private val pref: SeriesPreferences) {

    suspend operator fun invoke(): SortOption = pref.sort.first()
}
