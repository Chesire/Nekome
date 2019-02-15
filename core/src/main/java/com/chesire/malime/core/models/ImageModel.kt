package com.chesire.malime.core.models

data class ImageModel(
    val tiny: ImageData,
    val small: ImageData,
    val medium: ImageData,
    val large: ImageData
)

data class ImageData(
    val url: String,
    val width: Int,
    val height: Int
)
