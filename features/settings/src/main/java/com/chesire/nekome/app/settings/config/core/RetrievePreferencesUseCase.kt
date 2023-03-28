package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.core.preferences.SeriesPreferences
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

class RetrievePreferencesUseCase @Inject constructor(private val pref: SeriesPreferences) {

    operator fun invoke(): Flow<PreferenceModel> {
        return flowOf(PreferenceModel(false))
            .combine(pref.rateSeriesOnCompletion) { model, rateSeriesOnCompletion ->
                model.copy(shouldRateSeries = rateSeriesOnCompletion)
            }
    }
}

data class PreferenceModel(
    val shouldRateSeries: Boolean
)
