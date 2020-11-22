package com.chesire.nekome.kitsu.library.entity

import javax.inject.Inject

/**
 * Factory to create JSON representations of models to send via the api.
 */
class EntityFactory @Inject constructor() {

    /**
     * Creates a new JSON entity for adding a new series.
     */
    fun createAddEntity(
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
     * Creates a new JSON entity for updating a series.
     */
    fun createUpdateEntity(
        userSeriesId: Int,
        newProgress: Int,
        newStatus: String
    ) =
        """
{
  "data": {
    "id": $userSeriesId,
    "type": "libraryEntries",
    "attributes": {
      "progress": $newProgress,
      "status": "$newStatus"
    }
  }
}
        """.trimIndent()
}
