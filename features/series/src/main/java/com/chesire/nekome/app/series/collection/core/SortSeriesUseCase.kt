@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.SortOption
import com.chesire.nekome.datasource.series.SeriesDomain
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class SortSeriesUseCase @Inject constructor(private val pref: SeriesPreferences) {

    operator fun invoke(seriesList: List<SeriesDomain>): Flow<List<SeriesDomain>> {
        return pref.sort.flatMapLatest { sortOption ->
            flow {
                Timber.d("Sorting series by [$sortOption]")
                val sortedSeries = seriesList.sortedWith(
                    when (sortOption) {
                        SortOption.Default -> compareBy { it.userId }
                        SortOption.Title -> compareBy { it.title }
                        SortOption.StartDate -> compareBy { it.startDate }
                        SortOption.EndDate -> compareBy { it.endDate }
                        SortOption.Rating -> compareBy { it.rating }
                    }
                )

                emit(sortedSeries)
            }
        }
    }
}
