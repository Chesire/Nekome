package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.datasource.series.SeriesRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class RefreshSeriesUseCase @Inject constructor(private val repo: SeriesRepository) {

    suspend operator fun invoke(): Result<Unit, Unit> {
        return withContext(Dispatchers.IO) {
            val animeJob = async(Dispatchers.IO) { repo.refreshAnime() }
            val mangaJob = async(Dispatchers.IO) { repo.refreshManga() }

            val results = awaitAll(animeJob, mangaJob)
            if (results.all { it is Resource.Success }) {
                Ok(Unit)
            } else {
                Err(Unit)
            }
        }
    }
}
