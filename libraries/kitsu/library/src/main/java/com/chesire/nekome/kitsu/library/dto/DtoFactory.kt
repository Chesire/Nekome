package com.chesire.nekome.kitsu.library.dto

import org.json.JSONObject
import javax.inject.Inject

/**
 * Factory to create JSON representations of models to send via the api.
 */
class DtoFactory @Inject constructor() {

    /**
     * Creates a new JSON DTO for adding a new series.
     */
    fun createAddDto(
        userId: Int,
        seriesId: Int,
        startingStatus: String,
        seriesType: String
    ) =
        """
{
  "data": {
    "type": "libraryEntries",
    "attributes": {
      "progress": 0,
      "status": "$startingStatus"
    },
    "relationships": {
      "$seriesType": {
        "data": {
          "type": "$seriesType",
          "id": $seriesId
        }
      },
      "user": {
        "data": {
          "type": "users",
          "id": $userId
        }
      }
    }
  }
}
        """.trimIndent()

    /**
     * Creates a new JSON DTO for updating a series.
     */
    fun createUpdateDto(
        userSeriesId: Int,
        newProgress: Int,
        newStatus: String,
        rating: Int
    ): String {
        val attributesObject = JSONObject()
            .apply {
                put("progress", newProgress)
                put("status", newStatus)
                if (rating > 1) put("ratingTwenty", rating)
            }
        val dataObject = JSONObject()
            .apply {
                put("id", userSeriesId)
                put("type", "libraryEntries")
                put("attributes", attributesObject)
            }
        return JSONObject().put("data", dataObject).toString()
    }
}
