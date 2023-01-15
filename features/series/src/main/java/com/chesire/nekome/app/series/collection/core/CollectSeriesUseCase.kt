package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.datasource.series.SeriesDomain
import com.chesire.nekome.datasource.series.SeriesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class CollectSeriesUseCase @Inject constructor(private val seriesRepo: SeriesRepository) {

    operator fun invoke(): Flow<List<SeriesDomain>> = seriesRepo.getSeries()
}
