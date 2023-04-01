package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.core.preferences.SeriesPreferences
import javax.inject.Inject
import timber.log.Timber

class UpdateRateSeriesUseCase @Inject constructor(private val pref: SeriesPreferences) {

    suspend operator fun invoke(newRateSeriesValue: Boolean) {
        Timber.i("Updating rate series to [$newRateSeriesValue]")
        pref.updateRateSeriesOnCompletion(newRateSeriesValue)
    }
}
