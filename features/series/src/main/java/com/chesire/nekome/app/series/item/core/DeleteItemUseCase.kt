package com.chesire.nekome.app.series.item.core

import com.chesire.nekome.datasource.series.SeriesRepository
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteItemUseCase @Inject constructor(private val seriesRepo: SeriesRepository) {

    suspend operator fun invoke(userSeriesId: Int): Result<Unit, Unit> {
        return withContext(Dispatchers.IO) {
            val series = seriesRepo.getSeries(userSeriesId)

            seriesRepo.deleteSeries(series)
                .mapError { }
        }
    }
}
