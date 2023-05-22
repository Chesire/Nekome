package com.chesire.nekome.app.search.host.core

import com.chesire.nekome.datasource.series.SeriesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class RetrieveUserSeriesIdsUseCase @Inject constructor(private val seriesRepo: SeriesRepository) {

    operator fun invoke(): Flow<List<Int>> {
        return seriesRepo
            .getSeries()
            .mapLatest { seriesDomains ->
                seriesDomains.map { it.id }
            }
    }
}
