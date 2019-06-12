package com.chesire.malime.kitsu

import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.kitsu.adapters.UserSeriesStatusAdapter

private val userSeriesStatusAdapter = UserSeriesStatusAdapter()

@Suppress("LongMethod")
internal fun createNewAddModel(
    userId: Int,
    seriesId: Int,
    startingStatus: UserSeriesStatus,
    seriesType: String
) = """
{
  "data": {
    "type": "libraryEntries",
    "attributes": {
      "progress": 0,
      "status": "${userSeriesStatusAdapter.userSeriesStatusToString(startingStatus)}"
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
}""".trimIndent()

internal fun createUpdateModel(
    userSeriesId: Int,
    newProgress: Int,
    newStatus: UserSeriesStatus
) = """
{
  "data": {
    "id": $userSeriesId,
    "type": "libraryEntries",
    "attributes": {
      "progress": $newProgress,
      "status": "${userSeriesStatusAdapter.userSeriesStatusToString(newStatus)}"
    }
  }
}""".trimIndent()
