package com.chesire.nekome.kitsu

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.kitsu.adapters.UserSeriesStatusAdapter
import com.chesire.nekome.kitsu.api.intermediaries.SeriesItem
import com.chesire.nekome.kitsu.api.library.LibraryEntry

private val userSeriesStatusAdapter = UserSeriesStatusAdapter()

/**
 * Creates the json required for adding a new series to be tracked.
 */
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

/**
 * Creates the json required for updating a tracked series.
 */
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

/**
 * Creates a [SeriesModel] from the information stored in [library] and [series].
 */
internal fun createSeriesModel(library: LibraryEntry, series: SeriesItem) = SeriesModel(
    id = series.id,
    userId = library.id,
    type = series.type,
    subtype = series.attributes.subtype,
    slug = series.attributes.slug,
    title = series.attributes.canonicalTitle,
    seriesStatus = series.attributes.status,
    userSeriesStatus = library.attributes.status,
    progress = library.attributes.progress,
    totalLength = series.attributes.episodeCount
        ?: series.attributes.chapterCount
        ?: 0,
    posterImage = series.attributes.posterImage ?: ImageModel.empty,
    coverImage = series.attributes.coverImage ?: ImageModel.empty,
    nsfw = series.attributes.nsfw,
    startDate = series.attributes.startDate ?: "",
    endDate = series.attributes.endDate ?: ""
)
