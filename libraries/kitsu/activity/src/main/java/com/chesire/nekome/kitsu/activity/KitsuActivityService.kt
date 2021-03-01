package com.chesire.nekome.kitsu.activity

import com.chesire.nekome.kitsu.activity.dto.RetrieveActivityDto
import retrofit2.Response
import retrofit2.http.GET

interface KitsuActivityService {

    @GET("api/edge/library-events")
    suspend fun retrieveActivity(): Response<RetrieveActivityDto>
}
