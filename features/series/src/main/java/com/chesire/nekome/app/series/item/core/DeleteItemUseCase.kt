package com.chesire.nekome.app.series.item.core

import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.series.SeriesRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteItemUseCase @Inject constructor(private val seriesRepo: SeriesRepository) {

    suspend operator fun invoke(userSeriesId: Int): Result<Unit, Unit> {
        return withContext(Dispatchers.IO) {
            val series = seriesRepo.getSeries(userSeriesId)

            when (seriesRepo.deleteSeries(series)) {
                is Resource.Success -> Ok(Unit)
                is Resource.Error -> Err(Unit)
            }
        }
    }
}
