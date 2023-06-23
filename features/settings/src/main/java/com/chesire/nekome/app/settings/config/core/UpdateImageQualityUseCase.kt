package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.ImageQuality
import javax.inject.Inject
import timber.log.Timber

class UpdateImageQualityUseCase @Inject constructor(private val pref: SeriesPreferences) {

    suspend operator fun invoke(newImageQuality: ImageQuality) {
        Timber.i("Updating image quality to [$newImageQuality]")
        pref.updateImageQuality(newImageQuality)
    }
}
