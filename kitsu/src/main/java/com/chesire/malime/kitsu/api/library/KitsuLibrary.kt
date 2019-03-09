package com.chesire.malime.kitsu.api.library

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.kitsu.adapters.UserSeriesStatusAdapter
import com.chesire.malime.kitsu.parse
import com.chesire.malime.kitsu.parseError
import kotlinx.coroutines.Deferred
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

private const val LIMIT = 500
private const val MAX_RETRIES = 3
private const val ANIME_TYPE = "anime"
private const val MANGA_TYPE = "manga"

class KitsuLibrary @Inject constructor(
    private val libraryService: KitsuLibraryService,
    @Named("userId")
    private val userId: Int
) : LibraryApi {
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

        return try {
            libraryService.updateItemAsync(userSeriesId, body).await().parse()
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    override suspend fun delete(userSeriesId: Int): Resource<Any> {
        return try {
            val response = libraryService.deleteItemAsync(userSeriesId).await()
            return if (response.isSuccessful) {
                Resource.Success(Any())
            } else {
                response.parseError()
            }
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    @Suppress("ComplexMethod")
    private suspend fun performRetrieveCall(
        execute: (Int, Int, Int) -> Deferred<Response<ParsedRetrieveResponse>>
    ): Resource<List<SeriesModel>> {
        val models = mutableListOf<SeriesModel>()

        var offset = 0
        var page = 0
        var retries = 0
        var executeAgain: Boolean
        var errorResponse = Resource.Error<ParsedRetrieveResponse>("", 200)

        do {
            executeAgain = false

            val response: Response<ParsedRetrieveResponse>
            try {
                response = execute(userId, offset, LIMIT).await()
            } catch (ex: Exception) {
                return ex.parse()
            }

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
                    errorResponse = response.parseError()
                }
            }
        } while (executeAgain)

        return if (retries == MAX_RETRIES && models.count() == 0) {
            Resource.Error(errorResponse.msg, errorResponse.code)
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
        return try {
            execute(body).await().parse()
        } catch (ex: Exception) {
            ex.parse()
        }
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
