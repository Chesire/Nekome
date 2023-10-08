package com.chesire.nekome.feature.serieswidget.core

import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.datasource.series.SeriesDomain
import com.chesire.nekome.datasource.series.SeriesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RetrieveSeriesUseCase @Inject constructor(
    private val seriesRepository: SeriesRepository
) {

    operator fun invoke(): Flow<List<SeriesDomain>> {
        return seriesRepository
            .getSeries()
            .map { seriesList ->
                seriesList
                    .filter { series -> series.type == SeriesType.Anime }
                    .filter { series -> series.userSeriesStatus == UserSeriesStatus.Current }
                    .filter { series -> series.totalLength == 0 || series.progress < series.totalLength }
            }
    }
}
