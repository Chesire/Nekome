package com.chesire.nekome.kitsu.library

import com.chesire.nekome.core.EntityMapper
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.kitsu.adapters.UserSeriesStatusAdapter
import com.chesire.nekome.kitsu.asError
import com.chesire.nekome.kitsu.library.entity.EntityFactory
import com.chesire.nekome.kitsu.library.entity.KitsuLibraryEntities
import com.chesire.nekome.kitsu.library.entity.KitsuLibraryEntity
import com.chesire.nekome.kitsu.parse
import com.chesire.nekome.kitsu.parseError
import com.chesire.nekome.library.api.LibraryApi
import com.chesire.nekome.library.api.LibraryEntity
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject
import kotlin.reflect.KSuspendFunction3

private const val LIMIT = 500
private const val MAX_RETRIES = 3
private const val ANIME_TYPE = "anime"
private const val MANGA_TYPE = "manga"

/**
 * Implementation of the [LibraryApi] for usage with the Kitsu API.
 */
class KitsuLibrary @Inject constructor(
    private val libraryService: KitsuLibraryService,
    private val map: EntityMapper<KitsuLibraryEntity, LibraryEntity>,
    private val entityFactory: EntityFactory
) : LibraryApi {

    private val userSeriesStatusAdapter = UserSeriesStatusAdapter()

    override suspend fun retrieveAnime(userId: Int): Resource<List<LibraryEntity>> =
        performRetrieveCall(userId, libraryService::retrieveAnimeAsync)

    override suspend fun retrieveManga(userId: Int): Resource<List<LibraryEntity>> =
        performRetrieveCall(userId, libraryService::retrieveMangaAsync)

    override suspend fun addAnime(
        userId: Int,
        seriesId: Int,
        startingStatus: UserSeriesStatus
    ): Resource<LibraryEntity> {
        val addJson = entityFactory.createAddEntity(
            userId,
            seriesId,
            userSeriesStatusAdapter.userSeriesStatusToString(startingStatus),
            ANIME_TYPE
        )
        val body = RequestBody.create(MediaType.parse("application/vnd.api+json"), addJson)

        return try {
            return parseResponse(libraryService.addAnimeAsync(body))
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    override suspend fun addManga(
        userId: Int,
        seriesId: Int,
        startingStatus: UserSeriesStatus
    ): Resource<LibraryEntity> {
        val addJson = entityFactory.createAddEntity(
            userId,
            seriesId,
            userSeriesStatusAdapter.userSeriesStatusToString(startingStatus),
            MANGA_TYPE
        )
        val body = RequestBody.create(MediaType.parse("application/vnd.api+json"), addJson)

        return try {
            return parseResponse(libraryService.addMangaAsync(body))
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    override suspend fun update(
        userSeriesId: Int,
        progress: Int,
        newStatus: UserSeriesStatus
    ): Resource<LibraryEntity> {
        val updateJson = entityFactory.createUpdateEntity(
            userSeriesId,
            progress,
            userSeriesStatusAdapter.userSeriesStatusToString(newStatus)
        )
        val body = RequestBody.create(MediaType.parse("application/vnd.api+json"), updateJson)

        return try {
            parseResponse(libraryService.updateItemAsync(userSeriesId, body))
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
            Response<KitsuLibraryEntities>>
    ): Resource<List<LibraryEntity>> {
        val models = mutableListOf<LibraryEntity>()

        var offset = 0
        var page = 0
        var retries = 0
        var executeAgain: Boolean
        var errorResponse = Resource.Error<KitsuLibraryEntities>("", 200)

        do {
            executeAgain = false

            val response: Response<KitsuLibraryEntities>
            try {
                response = execute(userId, offset, LIMIT)
            } catch (ex: Exception) {
                return ex.parse()
            }

            val body = response.body()
            if (response.isSuccessful && body != null) {
                val series = body.data.map {
                    map.from(KitsuLibraryEntity(it, body.included))
                }
                models.addAll(series)
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

    private fun parseResponse(response: Response<KitsuLibraryEntity>): Resource<LibraryEntity> {
        return if (response.isSuccessful) {
            response.body()?.let { entity ->
                Resource.Success(map.from(entity))
            } ?: Resource.Error.emptyResponse()
        } else {
            response.asError()
        }
    }
}
