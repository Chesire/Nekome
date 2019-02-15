package com.chesire.malime.kitsu.models.response.parsing

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ParsingImageModel(
    @Json(name = "tiny")
    val tiny: String = "",
    @Json(name = "small")
    val small: String = "",
    @Json(name = "medium")
    val medium: String = "",
    @Json(name = "large")
    val large: String = "",
    @Json(name = "meta")
    val meta: ImageMeta
) {
    @JsonClass(generateAdapter = true)
    data class ImageMeta(
        @Json(name = "dimensions")
        val dimensions: DimensionsMeta
    ) {
        @JsonClass(generateAdapter = true)
        data class DimensionsMeta(
            @Json(name = "tiny")
            val tiny: DimensionsData?,
            @Json(name = "small")
            val small: DimensionsData?,
            @Json(name = "medium")
            val medium: DimensionsData?,
            @Json(name = "large")
            val large: DimensionsData?
        ) {
            @JsonClass(generateAdapter = true)
            data class DimensionsData(
                @Json(name = "width")
                val width: Int = 0,
                @Json(name = "height")
                val height: Int = 0
            )
        }
    }
}
