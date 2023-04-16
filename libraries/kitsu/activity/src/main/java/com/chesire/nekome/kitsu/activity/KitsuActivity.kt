package com.chesire.nekome.kitsu.activity

import com.chesire.nekome.core.models.ErrorDomain
import com.chesire.nekome.datasource.activity.ActivityDomain
import com.chesire.nekome.datasource.activity.remote.ActivityApi
import com.chesire.nekome.kitsu.activity.dto.RetrieveActivityDto
import com.chesire.nekome.kitsu.asError
import com.chesire.nekome.kitsu.parse
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
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

    override suspend fun retrieveActivity(): Result<List<ActivityDomain>, ErrorDomain> {
        return try {
            parseResponse(activityService.retrieveLibraryEvents())
        } catch (ex: Exception) {
            Err(ex.parse())
        }
    }

    private fun parseResponse(
        response: Response<RetrieveActivityDto>
    ): Result<List<ActivityDomain>, ErrorDomain> {
        return if (response.isSuccessful) {
            response.body()?.let { activity ->
                Ok(activity.data.map { map.toActivityDomain(it) })
            } ?: Err(ErrorDomain.emptyResponse)
        } else {
            Err(response.asError())
        }
    }
}
