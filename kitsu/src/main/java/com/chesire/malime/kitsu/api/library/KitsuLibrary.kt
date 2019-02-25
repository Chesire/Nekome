package com.chesire.malime.kitsu.api.library

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.kitsu.adapters.UserSeriesStatusAdapter
import com.chesire.malime.kitsu.api.ResponseParser
import kotlinx.coroutines.Deferred
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

private const val LIMIT = 500
private const val MAX_RETRIES = 3
private const val ANIME_TYPE = "anime"
private const val MANGA_TYPE = "manga"

class KitsuLibrary @Inject constructor(
    private val libraryService: KitsuLibraryService,
    private val userId: Int
) : ResponseParser,
    LibraryApi {

    private val userSeriesStatusAdapter = UserSeriesStatusAdapter()

    override suspend fun retrieveAnime() = performRetrieveCall(libraryService::retrieveAnimeAsync)
    override suspend fun retrieveManga() = performRetrieveCall(libraryService::retrieveMangaAsync)

    override suspend fun addAnime(seriesId: Int, startingStatus: UserSeriesStatus) =
        performAddCall(seriesId, startingStatus, ANIME_TYPE, libraryService::addAnimeAsync)

    override suspend fun addManga(seriesId: Int, startingStatus: UserSeriesStatus) =
        performAddCall(seriesId, startingStatus, MANGA_TYPE, libraryService::addMangaAsync)

    override suspend fun update(
        userSeriesId: Int,
        progress: Int,
        newStatus: UserSeriesStatus
    ): Resource<SeriesModel> {
        val updateModelJson = createUpdateModel(userSeriesId, progress, newStatus)
        val body = RequestBody.create(MediaType.parse("application/vnd.api+json"), updateModelJson)

        val response = libraryService.updateItemAsync(userSeriesId, body).await()
        return parseResponse(response)
    }

    override suspend fun delete(userSeriesId: Int): Resource<Any> {
        val response = libraryService.deleteItemAsync(userSeriesId).await()
        return if (response.isSuccessful) {
            Resource.Success(Any())
        } else {
            Resource.Error(response.errorBody()?.string() ?: response.message())
        }
    }

    private suspend fun performRetrieveCall(
        execute: (Int, Int, Int) -> Deferred<Response<ParsedRetrieveResponse>>
    ): Resource<List<SeriesModel>> {
        val models = mutableListOf<SeriesModel>()

        var offset = 0
        var page = 0
        var retries = 0
        var errorMessage = ""
        var executeAgain: Boolean

        do {
            executeAgain = false

            val response = execute(userId, offset, LIMIT).await()
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                models.addAll(body.series)
                retries = 0

                if (!body.links.next.isEmpty()) {
                    page++
                    offset = LIMIT * page
                    executeAgain = true
                }
            } else {
                if (retries < MAX_RETRIES) {
                    retries++
                    executeAgain = true
                } else {
                    errorMessage = response.errorBody()?.string() ?: response.message()
                }
            }
        } while (executeAgain)

        return if (retries == MAX_RETRIES && models.count() == 0) {
            Resource.Error(errorMessage)
        } else {
            Resource.Success(models)
        }
    }

    private suspend fun performAddCall(
        seriesId: Int,
        startingStatus: UserSeriesStatus,
        type: String,
        execute: (RequestBody) -> Deferred<Response<SeriesModel>>
    ): Resource<SeriesModel> {
        val addModelJson = createNewAddModel(seriesId, startingStatus, type)
        val body = RequestBody.create(MediaType.parse("application/vnd.api+json"), addModelJson)
        val response = execute(body).await()

        return parseResponse(response)
    }

    private fun createNewAddModel(
        seriesId: Int,
        startingStatus: UserSeriesStatus,
        seriesType: String
    ): String {
        return """
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
    }

    private fun createUpdateModel(
        userSeriesId: Int,
        newProgress: Int,
        newStatus: UserSeriesStatus
    ): String {
        return """
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
    }
}
