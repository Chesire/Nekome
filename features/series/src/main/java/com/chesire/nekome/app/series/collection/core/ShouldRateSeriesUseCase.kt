package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.datasource.series.SeriesRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class ShouldRateSeriesUseCase @Inject constructor(
    private val repo: SeriesRepository,
    private val pref: SeriesPreferences
) {

    suspend operator fun invoke(userSeriesId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val domain = repo.getSeries(userSeriesId)
            pref.rateSeriesOnCompletion.first() && domain.progress + 1 == domain.totalLength
        }
    }
}
