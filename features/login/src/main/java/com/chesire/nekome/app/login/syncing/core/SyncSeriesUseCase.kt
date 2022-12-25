package com.chesire.nekome.app.login.syncing.core

import com.chesire.nekome.datasource.series.SeriesRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class SyncSeriesUseCase @Inject constructor(private val seriesRepo: SeriesRepository) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        val animeJob = async(Dispatchers.IO) { seriesRepo.refreshAnime() }
        val mangaJob = async(Dispatchers.IO) { seriesRepo.refreshManga() }

        awaitAll(animeJob, mangaJob)
    }
}
