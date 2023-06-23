package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
import com.chesire.nekome.core.preferences.flags.ImageQuality
import com.chesire.nekome.core.preferences.flags.Theme
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class RetrievePreferencesUseCase @Inject constructor(
    private val applicationPref: ApplicationPreferences,
    private val seriesPref: SeriesPreferences
) {

    operator fun invoke(): Flow<PreferenceModel> {
        return combine(
            applicationPref.theme,
            applicationPref.defaultHomeScreen,
            applicationPref.defaultSeriesState,
            seriesPref.rateSeriesOnCompletion,
            seriesPref.imageQuality
        ) { theme, defaultHomeScreen, defaultSeriesState, shouldRateSeries, imageQuality ->
            PreferenceModel(
                theme = theme,
                defaultHomeScreen = defaultHomeScreen,
                defaultSeriesStatus = defaultSeriesState,
                shouldRateSeries = shouldRateSeries,
                imageQuality = imageQuality
            )
        }
    }
}

data class PreferenceModel(
    val theme: Theme,
    val defaultHomeScreen: HomeScreenOptions,
    val defaultSeriesStatus: UserSeriesStatus,
    val shouldRateSeries: Boolean,
    val imageQuality: ImageQuality
)
