package com.chesire.nekome.feature.serieswidget.core

import com.chesire.nekome.datasource.series.SeriesRepository
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapEither
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateSeriesUseCase @Inject constructor(
    private val seriesRepository: SeriesRepository
) {

    suspend operator fun invoke(seriesId: Int): Result<Unit, Unit> {
        val currentSeries = seriesRepository.getSeries(seriesId)
        return withContext(Dispatchers.IO) {
            seriesRepository
                .updateSeries(
                    currentSeries.userId,
                    currentSeries.progress + 1,
                    currentSeries.userSeriesStatus,
                    currentSeries.rating
                )
                .mapEither(
                    success = { Unit },
                    failure = { Unit }
                )
        }
    }
}
