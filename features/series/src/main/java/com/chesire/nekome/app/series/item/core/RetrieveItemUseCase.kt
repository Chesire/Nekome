package com.chesire.nekome.app.series.item.core

import com.chesire.nekome.datasource.series.SeriesDomain
import com.chesire.nekome.datasource.series.SeriesRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrieveItemUseCase @Inject constructor(private val seriesRepo: SeriesRepository) {

    suspend operator fun invoke(userSeriesId: Int): SeriesDomain = withContext(Dispatchers.IO) {
        seriesRepo.getSeries(userSeriesId)
    }
}
