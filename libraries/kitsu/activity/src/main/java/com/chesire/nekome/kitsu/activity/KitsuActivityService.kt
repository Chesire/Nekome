package com.chesire.nekome.kitsu.activity

import com.chesire.nekome.kitsu.activity.dto.RetrieveActivityDto
import retrofit2.Response
import retrofit2.http.GET

/**
 * Constructed with Retrofit to interface with the Kitsu API for queries related to user activity.
 */
interface KitsuActivityService {

    /**
     * Retrieves the recent library events for a user.
     */
    @GET("api/edge/library-events")
    suspend fun retrieveLibraryEvents(): Response<RetrieveActivityDto>
}
