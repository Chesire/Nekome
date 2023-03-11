@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.app.series.SeriesPreferences
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.datasource.series.SeriesDomain
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class FilterSeriesUseCase @Inject constructor(private val pref: SeriesPreferences) {

    operator fun invoke(
        seriesList: List<SeriesDomain>,
        seriesType: SeriesType
    ): Flow<List<SeriesDomain>> {
        return pref.filter.flatMapLatest { filter ->
            flow {
                Timber.d("Filtering series with [$filter]")
                val filteredSeries = seriesList
                    .filter { it.type == seriesType }
                    .filter { filter[it.userSeriesStatus.index] ?: false }

                emit(filteredSeries)
            }
        }
    }
}
