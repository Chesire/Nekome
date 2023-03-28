package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.Theme
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

class RetrievePreferencesUseCase @Inject constructor(
    private val applicationPref: ApplicationPreferences,
    private val seriesPref: SeriesPreferences
) {

    operator fun invoke(): Flow<PreferenceModel> {
        // This is not performant, and causes everything beneath it to run every time...
        // need a better way to observe these flows together
        return flowOf(
            PreferenceModel(
                theme = Theme.System,
                shouldRateSeries = false
            )
        ).combine(seriesPref.rateSeriesOnCompletion) { model, rateSeriesOnCompletion ->
            model.copy(shouldRateSeries = rateSeriesOnCompletion)
        }.combine(applicationPref.theme) { model, theme ->
            model.copy(theme = theme)
        }
    }
}

data class PreferenceModel(
    val theme: Theme,
    val shouldRateSeries: Boolean
)
