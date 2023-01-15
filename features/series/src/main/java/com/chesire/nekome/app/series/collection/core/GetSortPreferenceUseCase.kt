package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.app.series.SeriesPreferences
import com.chesire.nekome.core.flags.SortOption
import javax.inject.Inject

class GetSortPreferenceUseCase @Inject constructor(
    private val seriesPreferences: SeriesPreferences
) {

    operator fun invoke(): SortOption = seriesPreferences.sortPreference
}
