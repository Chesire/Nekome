package com.chesire.malime.kitsu.api.library

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.models.SeriesModel

private const val LIMIT = 500
private const val MAX_RETRIES = 3

class KitsuLibrary(
    private val libraryService: KitsuLibraryService,
    private val userId: Int
) : LibraryApi {
    override suspend fun retrieveAnime(): Resource<List<SeriesModel>> {
        val models = mutableListOf<SeriesModel>()

        var offset = 0
        var page = 0
        var retries = 0
        var errorMessage = ""

        while (true) {
            val result = libraryService.retrieveAnimeAsync(userId, offset, LIMIT).await()
            if (result.isSuccessful && result.body() != null) {
                val body = result.body()!!
                models.addAll(body.series)
                retries = 0

                if (!body.links.next.isEmpty()) {
                    page++
                    offset = LIMIT * page
                    continue
                }
            } else {
                if (retries < MAX_RETRIES) {
                    retries++
                    continue
                } else {
                    errorMessage = result.message()
                }
            }

            break
        }

        return if (retries == MAX_RETRIES && models.count() == 0) {
            Resource.Error(errorMessage)
        } else {
            Resource.Success(models)
        }
    }

    override suspend fun retrieveManga(): Resource<List<SeriesModel>> {
        return Resource.Error("")
    }
}
