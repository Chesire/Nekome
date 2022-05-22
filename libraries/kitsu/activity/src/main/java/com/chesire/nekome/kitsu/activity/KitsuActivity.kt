package com.chesire.nekome.kitsu.activity

import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.activity.ActivityDomain
import com.chesire.nekome.datasource.activity.remote.ActivityApi
import com.chesire.nekome.kitsu.activity.dto.RetrieveActivityDto
import com.chesire.nekome.kitsu.asError
import com.chesire.nekome.kitsu.parse
import javax.inject.Inject
import retrofit2.Response

/**
 * Implementation of the [ActivityApi] for usage with the Kitsu API.
 */
@Suppress("TooGenericExceptionCaught")
class KitsuActivity @Inject constructor(
    private val activityService: KitsuActivityService,
    private val map: RetrieveActivityDtoMapper
) : ActivityApi {

    override suspend fun retrieveActivity(): Resource<List<ActivityDomain>> {
        return try {
            parseResponse(activityService.retrieveLibraryEvents())
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    private fun parseResponse(
        response: Response<RetrieveActivityDto>
    ): Resource<List<ActivityDomain>> {
        return if (response.isSuccessful) {
            response.body()?.let { activity ->
                Resource.Success(activity.data.map { map.toActivityDomain(it) })
            } ?: Resource.Error.emptyResponse()
        } else {
            response.asError()
        }
    }
}
