package com.chesire.nekome.kitsu.library.entity

class EntityFactory {

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
