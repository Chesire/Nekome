package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.series.SeriesDomain
import com.chesire.nekome.datasource.series.SeriesRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class IncrementSeriesUseCase @Inject constructor(private val repo: SeriesRepository) {

    suspend operator fun invoke(userSeriesId: Int): Result<SeriesDomain, Unit> {
        return withContext(Dispatchers.IO) {
            val domain = requireNotNull(repo.getSeries().first().find { it.userId == userSeriesId })
            val result = repo.updateSeries(
                userSeriesId = domain.userId,
                progress = domain.progress + 1,
                status = domain.userSeriesStatus,
                rating = domain.rating
            )

            if (result is Resource.Success) {
                Ok(result.data)
            } else {
                Err(Unit)
            }
        }
    }
}