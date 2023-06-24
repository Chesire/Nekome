package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
import com.chesire.nekome.core.preferences.flags.ImageQuality
import com.chesire.nekome.core.preferences.flags.Theme
import com.chesire.nekome.core.preferences.flags.TitleLanguage
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
            seriesPref.imageQuality,
            seriesPref.titleLanguage
        ) { theme,
            defaultHomeScreen,
            defaultSeriesState,
            shouldRateSeries,
            imageQuality,
            titleLanguage ->
            PreferenceModel(
                theme = theme,
                defaultHomeScreen = defaultHomeScreen,
                defaultSeriesStatus = defaultSeriesState,
                shouldRateSeries = shouldRateSeries,
                imageQuality = imageQuality,
                titleLanguage = titleLanguage
            )
        }
    }
}

data class PreferenceModel(
    val theme: Theme,
    val defaultHomeScreen: HomeScreenOptions,
    val defaultSeriesStatus: UserSeriesStatus,
    val shouldRateSeries: Boolean,
    val imageQuality: ImageQuality,
    val titleLanguage: TitleLanguage
)

@Suppress("LongParameterList")
private fun <T1, T2, T3, T4, T5, T6, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    transform: suspend (T1, T2, T3, T4, T5, T6) -> R
): Flow<R> = combine(
    combine(flow, flow2, flow3, ::Triple),
    combine(flow4, flow5, flow6, ::Triple)
) { t1, t2 ->
    transform(
        t1.first,
        t1.second,
        t1.third,
        t2.first,
        t2.second,
        t2.third
    )
}
