package com.chesire.nekome.kitsu.library.dto

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
    ) =
        """
{
  "data": {
    "id": $userSeriesId,
    "type": "libraryEntries",
    "attributes": {
      "progress": $newProgress,
      "status": "$newStatus",
      "ratingTwenty": $rating
    }
  }
}
        """.trimIndent()
}
