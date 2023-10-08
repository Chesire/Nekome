package com.chesire.nekome.feature.serieswidget.core

import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.SortOption
import com.chesire.nekome.datasource.series.SeriesDomain
import com.chesire.nekome.datasource.series.SeriesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class RetrieveSeriesUseCase @Inject constructor(
    private val seriesRepository: SeriesRepository,
    private val pref: SeriesPreferences
) {

    suspend operator fun invoke(): Flow<List<SeriesDomain>> {
        val sortOption = pref.sort.first()
        return seriesRepository
            .getSeries()
            .map { seriesList ->
                seriesList
                    .filter { series -> series.type == SeriesType.Anime }
                    .filter { series -> series.userSeriesStatus == UserSeriesStatus.Current }
                    .filter { series -> series.totalLength == 0 || series.progress < series.totalLength }
                    .sortedWith(
                        when (sortOption) {
                            SortOption.Default -> compareBy { it.userId }
                            SortOption.Title -> compareBy { it.title }
                            SortOption.StartDate -> compareBy { it.startDate }
                            SortOption.EndDate -> compareBy { it.endDate }
                            SortOption.Rating -> compareBy { it.rating }
                        }
                    )
            }
    }
}
