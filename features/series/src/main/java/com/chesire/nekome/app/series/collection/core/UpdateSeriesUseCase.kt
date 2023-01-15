package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.datasource.series.SeriesDomain
import com.chesire.nekome.datasource.series.SeriesRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateSeriesUseCase @Inject constructor(
    private val repo: SeriesRepository
) {

    suspend operator fun invoke(
        userSeriesId: Int,
        newProgress: Int,
        newUserSeriesStatus: UserSeriesStatus,
        rating: Int // A rating of 0 will be ignored
    ): Result<SeriesDomain, Unit> {
        return withContext(Dispatchers.IO) {
            val result = repo.updateSeries(
                userSeriesId = userSeriesId,
                progress = newProgress,
                status = newUserSeriesStatus,
                rating = rating
            )

            if (result is Resource.Success) {
                Ok(result.data)
            } else {
                Err(Unit)
            }
        }
    }
}
