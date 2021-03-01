package com.chesire.nekome.kitsu.activity.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RetrieveActivityDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "attributes")
    val attributes: Attributes
) {
    data class Attributes(
        @Json(name = "createdAt")
        val createdAt: String,
        @Json(name = "updatedAt")
        val updatedAt: String,
        @Json(name = "changedData")
        val changedData: String, // needs to be object
        @Json(name = "kind")
        val kind: Kind
    )
}

/*
            "changedData":{
               "rating":[
                  16,
                  15
               ]
            },
            "kind":"rated"

            "changedData":{
               "progress":[
                  62,
                  63
               ]
            },
            "kind":"progressed"

            "changedData":{
               "status":[
                  "planned",
                  "current"
               ]
            },
            "kind":"updated"
 */
