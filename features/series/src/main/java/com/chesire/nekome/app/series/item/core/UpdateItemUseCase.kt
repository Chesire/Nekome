package com.chesire.nekome.app.series.item.core

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.datasource.series.SeriesRepository
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapEither
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateItemUseCase @Inject constructor(private val seriesRepo: SeriesRepository) {

    suspend operator fun invoke(updateItemModel: UpdateItemModel): Result<Unit, Unit> {
        return withContext(Dispatchers.IO) {
            seriesRepo.updateSeries(
                userSeriesId = updateItemModel.userSeriesId,
                progress = updateItemModel.progress,
                volumesOwned = updateItemModel.volumesOwned,
                status = updateItemModel.newStatus,
                rating = updateItemModel.rating
            ).mapEither(
                success = {},
                failure = {}
            )
        }
    }
}

data class UpdateItemModel(
    val userSeriesId: Int,
    val progress: Int,
    val volumesOwned: Int?,
    val newStatus: UserSeriesStatus,
    val rating: Int
)
