package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.app.series.SeriesPreferences
import javax.inject.Inject

class GetFilterPreferenceUseCase @Inject constructor(
    private val seriesPreferences: SeriesPreferences
) {

    operator fun invoke(): Map<Int, Boolean> = seriesPreferences.filterPreference
}
