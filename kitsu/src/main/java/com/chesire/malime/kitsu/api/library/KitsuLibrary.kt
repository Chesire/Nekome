package com.chesire.malime.kitsu.api.library

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel
import kotlinx.coroutines.Deferred
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response

private const val LIMIT = 500
private const val MAX_RETRIES = 3

class KitsuLibrary(
    private val libraryService: KitsuLibraryService,
    private val userId: Int
) : LibraryApi {
    override suspend fun retrieveAnime() = performRetrieveCall(libraryService::retrieveAnimeAsync)
    override suspend fun retrieveManga() = performRetrieveCall(libraryService::retrieveMangaAsync)

    override suspend fun addAnime(model: SeriesModel): Resource<SeriesModel> {
        val addModelJson = createNewAddModel(userId, model.id, model.type)
        val body = RequestBody.create(MediaType.parse("application/vnd.api+json"), addModelJson)
        val response = libraryService.addAnime(body).await()

        if (response.isSuccessful && response.body() != null) {
            // create the SeriesModel
        }

        return Resource.Error("Working on it")
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
                    errorMessage = response.message()
                }
            }
        } while (executeAgain)

        return if (retries == MAX_RETRIES && models.count() == 0) {
            Resource.Error(errorMessage)
        } else {
            Resource.Success(models)
        }
    }
}

private fun createNewAddModel(userId: Int, seriesId: Int, seriesType: SeriesType): String {
    return """
{
  "data": {
    "type": "libraryEntries",
    "attributes": {
      "progress": 0,
      "status": ${UserSeriesStatus.Current}
    },
    "relationships": {
      "$seriesType": {
        "data": {
          "type": $seriesType,
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
