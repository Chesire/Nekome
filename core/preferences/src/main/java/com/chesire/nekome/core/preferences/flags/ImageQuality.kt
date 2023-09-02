package com.chesire.nekome.core.preferences.flags

import androidx.annotation.StringRes
import com.chesire.nekome.resources.StringResource

/**
 * Options available for the quality of the images.
 */
enum class ImageQuality(val index: Int, @StringRes val stringId: Int) {
    Low(0, StringResource.image_quality_low),
    Medium(1, StringResource.image_quality_medium),
    High(2, StringResource.image_quality_high);

    companion object {

        /**
         * Get [ImageQuality] for its given [index].
         */
        fun forIndex(index: Int): ImageQuality = values().find { it.index == index } ?: Low
    }
}
