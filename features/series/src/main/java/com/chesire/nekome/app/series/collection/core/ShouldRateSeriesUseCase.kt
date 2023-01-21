package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.app.series.SeriesPreferences
import com.chesire.nekome.datasource.series.SeriesRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class ShouldRateSeriesUseCase @Inject constructor(
    private val repo: SeriesRepository,
    private val preferences: SeriesPreferences
) {

    suspend operator fun invoke(userSeriesId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val domain = requireNotNull(repo.getSeries().first().find { it.userId == userSeriesId })
            preferences.rateSeriesOnCompletion && domain.progress + 1 == domain.totalLength
        }
    }
}
