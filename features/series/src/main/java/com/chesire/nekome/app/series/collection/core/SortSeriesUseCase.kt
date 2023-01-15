package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.app.series.SeriesPreferences
import com.chesire.nekome.core.flags.SortOption
import com.chesire.nekome.datasource.series.SeriesDomain
import javax.inject.Inject

class SortSeriesUseCase @Inject constructor(private val pref: SeriesPreferences) {

    operator fun invoke(seriesList: List<SeriesDomain>): List<SeriesDomain> {
        val sort = pref.sortPreference
        return seriesList.sortedWith(
            when (sort) {
                SortOption.Default -> compareBy { it.userId }
                SortOption.Title -> compareBy { it.title }
                SortOption.StartDate -> compareBy { it.startDate }
                SortOption.EndDate -> compareBy { it.endDate }
                SortOption.Rating -> compareBy { it.rating }
            }
        )
    }
}
