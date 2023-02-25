package com.chesire.nekome.app.series.item.core

import com.chesire.nekome.datasource.series.SeriesDomain
import com.chesire.nekome.datasource.series.SeriesRepository
import javax.inject.Inject

class RetrieveItemUseCase @Inject constructor(private val seriesRepo: SeriesRepository) {

    suspend operator fun invoke(userSeriesId: Int): SeriesDomain =
        seriesRepo.getSeries(userSeriesId)
}
