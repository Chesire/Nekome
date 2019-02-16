package com.chesire.malime.core.models

data class ImageModel(
    val tiny: ImageData,
    val small: ImageData,
    val medium: ImageData,
    val large: ImageData
) {
    companion object {
        val empty
            get() = ImageModel(
                tiny = ImageData.empty,
                small = ImageData.empty,
                medium = ImageData.empty,
                large = ImageData.empty
            )
    }
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
