package com.chesire.nekome.kitsu.api.library

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.kitsu.createNewAddModel
import com.chesire.nekome.kitsu.createUpdateModel
import com.chesire.nekome.kitsu.parse
import com.chesire.nekome.kitsu.parseError
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.LibraryApi
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject
import kotlin.reflect.KSuspendFunction1
import kotlin.reflect.KSuspendFunction3

private const val LIMIT = 500
private const val MAX_RETRIES = 3
private const val ANIME_TYPE = "anime"
private const val MANGA_TYPE = "manga"

/**
 * Provides an implementation of [LibraryApi] to interact with [KitsuLibrary] to manage a users
 * series.
 */
@Suppress("TooGenericExceptionCaught")
class KitsuLibrary @Inject constructor(
    private val libraryService: KitsuLibraryService
) : LibraryApi {

    override suspend fun retrieveAnime(userId: Int) =
        performRetrieveCall(userId, libraryService::retrieveAnimeAsync)

    override suspend fun retrieveManga(userId: Int) =
        performRetrieveCall(userId, libraryService::retrieveMangaAsync)

    override suspend fun addAnime(userId: Int, seriesId: Int, startingStatus: UserSeriesStatus) =
        performAddCall(userId, seriesId, startingStatus, ANIME_TYPE, libraryService::addAnimeAsync)

    override suspend fun addManga(userId: Int, seriesId: Int, startingStatus: UserSeriesStatus) =
        performAddCall(userId, seriesId, startingStatus, MANGA_TYPE, libraryService::addMangaAsync)

    override suspend fun update(
        userSeriesId: Int,
        progress: Int,
        newStatus: UserSeriesStatus
    ): Resource<SeriesModel> {
        val updateModelJson = createUpdateModel(userSeriesId, progress, newStatus)
        val body = RequestBody.create(MediaType.parse("application/vnd.api+json"), updateModelJson)

        return try {
            libraryService.updateItemAsync(userSeriesId, body).parse()
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    override suspend fun delete(userSeriesId: Int): Resource<Any> {
        return try {
            val response = libraryService.deleteItemAsync(userSeriesId)
            return if (response.isSuccessful) {
                Resource.Success(Any())
            } else {
                response.parseError()
            }
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    @Suppress("ComplexMethod", "LongMethod")
    private suspend fun performRetrieveCall(
        userId: Int,
        execute: KSuspendFunction3<
            @ParameterName(name = "userId") Int,
            @ParameterName(name = "offset") Int,
            @ParameterName(name = "limit") Int,
            Response<ParsedRetrieveResponse>>
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
                response = execute(userId, offset, LIMIT)
            } catch (ex: Exception) {
                return ex.parse()
            }

            val body = response.body()
            if (response.isSuccessful && body != null) {
                models.addAll(body.series)
                retries = 0

                if (body.links.next.isNotEmpty()) {
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
        userId: Int,
        seriesId: Int,
        startingStatus: UserSeriesStatus,
        type: String,
        execute: KSuspendFunction1<@ParameterName(name = "data") RequestBody, Response<SeriesModel>>
    ): Resource<SeriesModel> {
        val addModelJson = createNewAddModel(userId, seriesId, startingStatus, type)
        val body = RequestBody.create(MediaType.parse("application/vnd.api+json"), addModelJson)
        return try {
            execute(body).parse()
        } catch (ex: Exception) {
            ex.parse()
        }
    }
}
