package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.app.series.SeriesPreferences
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.datasource.series.SeriesDomain
import javax.inject.Inject

class FilterSeriesUseCase @Inject constructor(private val pref: SeriesPreferences) {

    operator fun invoke(
        seriesList: List<SeriesDomain>,
        seriesType: SeriesType
    ): List<SeriesDomain> {
        val filter = pref.filterPreference
        return seriesList
            .filter { it.type == seriesType }
            .filter { filter[it.userSeriesStatus.index] ?: false }
    }
}
