package com.chesire.nekome.app.series.item.core

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.datasource.series.SeriesRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateItemUseCase @Inject constructor(private val seriesRepo: SeriesRepository) {

    suspend operator fun invoke(updateItemModel: UpdateItemModel): Result<Unit, Unit> {
        return withContext(Dispatchers.IO) {
            val result = seriesRepo.updateSeries(
                userSeriesId = updateItemModel.userSeriesId,
                progress = updateItemModel.progress,
                status = updateItemModel.newStatus,
                rating = updateItemModel.rating
            )

            when (result) {
                is Resource.Success -> Ok(Unit)
                is Resource.Error -> Err(Unit)
            }
        }
    }
}

data class UpdateItemModel(
    val userSeriesId: Int,
    val progress: Int,
    val newStatus: UserSeriesStatus,
    val rating: Int
)
