package com.chesire.nekome.kitsu.library

import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.datasource.series.SeriesDomain
import com.chesire.nekome.datasource.series.remote.SeriesApi
import com.chesire.nekome.kitsu.asError
import com.chesire.nekome.kitsu.library.adapter.UserSeriesStatusAdapter
import com.chesire.nekome.kitsu.library.dto.AddResponseDto
import com.chesire.nekome.kitsu.library.dto.DtoFactory
import com.chesire.nekome.kitsu.library.dto.RetrieveResponseDto
import com.chesire.nekome.kitsu.parse
import com.chesire.nekome.kitsu.parseError
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

private const val LIMIT = 500
private const val MAX_RETRIES = 3
private const val ANIME_TYPE = "anime"
private const val MANGA_TYPE = "manga"
private const val MEDIA_TYPE = "application/vnd.api+json"

/**
 * Implementation of the [SeriesApi] for usage with the Kitsu API.
 */
@Suppress("TooGenericExceptionCaught")
class KitsuLibrary @Inject constructor(
    private val libraryService: KitsuLibraryService,
    private val map: ResponseDtoMapper,
    private val entityFactory: DtoFactory
) : SeriesApi {

    private val userSeriesStatusAdapter = UserSeriesStatusAdapter()

    override suspend fun retrieveAnime(userId: Int): Resource<List<SeriesDomain>> =
        performRetrieveCall(userId, libraryService::retrieveAnimeAsync)

    override suspend fun retrieveManga(userId: Int): Resource<List<SeriesDomain>> =
        performRetrieveCall(userId, libraryService::retrieveMangaAsync)

    override suspend fun addAnime(
        userId: Int,
        seriesId: Int,
        startingStatus: UserSeriesStatus
    ): Resource<SeriesDomain> {
        val addJson = entityFactory.createAddDto(
            userId,
            seriesId,
            userSeriesStatusAdapter.userSeriesStatusToString(startingStatus),
            ANIME_TYPE
        )
        val body = RequestBody.create(MediaType.parse(MEDIA_TYPE), addJson)

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
    ): Resource<SeriesDomain> {
        val addJson = entityFactory.createAddDto(
            userId,
            seriesId,
            userSeriesStatusAdapter.userSeriesStatusToString(startingStatus),
            MANGA_TYPE
        )
        val body = RequestBody.create(MediaType.parse(MEDIA_TYPE), addJson)

        return try {
            return parseResponse(libraryService.addMangaAsync(body))
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    override suspend fun update(
        userSeriesId: Int,
        progress: Int,
        newStatus: UserSeriesStatus,
        rating: Int
    ): Resource<SeriesDomain> {
        val updateJson = entityFactory.createUpdateDto(
            userSeriesId,
            progress,
            userSeriesStatusAdapter.userSeriesStatusToString(newStatus),
            rating
        )
        val body = RequestBody.create(MediaType.parse(MEDIA_TYPE), updateJson)

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
        execute: suspend (userId: Int, offset: Int, limit: Int) -> Response<RetrieveResponseDto>
    ): Resource<List<SeriesDomain>> {
        val models = mutableListOf<SeriesDomain>()

        var offset = 0
        var page = 0
        var retries = 0
        var executeAgain: Boolean
        var errorResponse = Resource.Error<RetrieveResponseDto>("", 200)

        do {
            executeAgain = false

            val response: Response<RetrieveResponseDto>
            try {
                response = execute(userId, offset, LIMIT)
            } catch (ex: Exception) {
                return ex.parse()
            }

            val body = response.body()
            if (response.isSuccessful && body != null) {
                val series = body.data.mapNotNull {
                    map.toSeriesDomain(AddResponseDto(it, body.included))
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

    private fun parseResponse(response: Response<AddResponseDto>): Resource<SeriesDomain> {
        return if (response.isSuccessful) {
            response.body()?.let { entity ->
                Resource.Success(requireNotNull(map.toSeriesDomain(entity)))
            } ?: Resource.Error.emptyResponse()
        } else {
            response.asError()
        }
    }
}
