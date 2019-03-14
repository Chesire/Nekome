package com.chesire.malime.core.models

data class ImageModel(
    val tiny: ImageData,
    val small: ImageData,
    val medium: ImageData,
    val large: ImageData
) {
    /**
     * The largest ImageData that contains a URL, returns null if no URL is valid.
     */
    val largest: ImageData?
        get() {
            return when {
                large.url.isNotEmpty() -> large
                medium.url.isNotEmpty() -> medium
                small.url.isNotEmpty() -> small
                tiny.url.isNotEmpty() -> tiny
                else -> null
            }
        }

    /**
     * The smallest ImageData that contains a URL, returns null if no URL is valid.
     */
    val smallest: ImageData?
        get() {
            return when {
                tiny.url.isNotEmpty() -> tiny
                small.url.isNotEmpty() -> small
                medium.url.isNotEmpty() -> medium
                large.url.isNotEmpty() -> large
                else -> null
            }
        }

    companion object {
        val empty
            get() = ImageModel(
                tiny = ImageData.empty,
                small = ImageData.empty,
                medium = ImageData.empty,
                large = ImageData.empty
            )
    }

    data class ImageData(
        val url: String,
        val width: Int,
        val height: Int
    ) {
        companion object {
            val empty
                get() = ImageData("", 0, 0)
        }
    }
}
