package com.chesire.nekome.core.models

import com.squareup.moshi.JsonClass

/**
 * Data for images.
 *
 * Images are stored as a model with smaller models for each of the sizes.
 */
@JsonClass(generateAdapter = true)
data class ImageModel(
    val tiny: ImageData,
    val small: ImageData,
    val medium: ImageData,
    val large: ImageData
) {

    /**
     * The largest [ImageData] that contains a URL, returns null if no URL is valid.
     */
    val largest: ImageData?
        get() = when {
            large.url.isNotEmpty() -> large
            medium.url.isNotEmpty() -> medium
            small.url.isNotEmpty() -> small
            tiny.url.isNotEmpty() -> tiny
            else -> null
        }

    /**
     * The [ImageData] that sits closest to the middle, returns null if no URL is valid.
     */
    val middlest: ImageData?
        get() = when {
            medium.url.isNotEmpty() -> medium
            small.url.isNotEmpty() -> small
            large.url.isNotEmpty() -> large
            tiny.url.isNotEmpty() -> tiny
            else -> null
        }

    /**
     * The smallest [ImageData] that contains a URL, returns null if no URL is valid.
     */
    val smallest: ImageData?
        get() = when {
            tiny.url.isNotEmpty() -> tiny
            small.url.isNotEmpty() -> small
            medium.url.isNotEmpty() -> medium
            large.url.isNotEmpty() -> large
            else -> null
        }

    companion object {
        /**
         * Provides an empty implementation of [ImageModel].
         */
        val empty
            get() = ImageModel(
                tiny = ImageData.empty,
                small = ImageData.empty,
                medium = ImageData.empty,
                large = ImageData.empty
            )
    }

    /**
     * Data for a single image type, containing the [url] it can be accessed at, also the [width]
     * and [height] it should be.
     */
    @JsonClass(generateAdapter = true)
    data class ImageData(
        val url: String,
        val width: Int,
        val height: Int
    ) {
        companion object {
            /**
             * Provides an empty implementation of [ImageData].
             */
            val empty
                get() = ImageData("", 0, 0)
        }
    }
}
