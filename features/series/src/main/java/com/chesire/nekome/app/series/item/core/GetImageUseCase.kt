package com.chesire.nekome.app.series.item.core

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.ImageQuality
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class GetImageUseCase @Inject constructor(private val pref: SeriesPreferences) {

    suspend operator fun invoke(images: ImageModel): String {
        return when (pref.imageQuality.first()) {
            ImageQuality.Low -> images.smallest?.url
            ImageQuality.Medium -> images.middlest?.url
            ImageQuality.High -> images.largest?.url
        } ?: ""
    }
}
