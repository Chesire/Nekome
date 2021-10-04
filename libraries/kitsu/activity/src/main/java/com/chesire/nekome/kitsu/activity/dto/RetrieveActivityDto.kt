package com.chesire.nekome.kitsu.activity.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO from the Kitsu activity endpoint.
 */
@JsonClass(generateAdapter = true)
data class RetrieveActivityDto(
    @Json(name = "data")
    val data: List<Data>
) {

    /**
     * Data of the Kitsu activity item.
     */
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "id")
        val id: Int,
        @Json(name = "attributes")
        val attributes: Attributes
    ) {

        /**
         * Attributes of the Kitsu activity item.
         */
        @JsonClass(generateAdapter = true)
        data class Attributes(
            @Json(name = "createdAt")
            val createdAt: String,
            @Json(name = "updatedAt")
            val updatedAt: String,
            @Json(name = "changedData")
            val changedData: ChangedDataContainer
        )
    }
}
