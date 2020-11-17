package com.chesire.nekome.kitsu.user

import retrofit2.Response
import retrofit2.http.GET

interface KitsuUserService {
    @GET("api/edge/users?filter[self]=true&fields[users]=id,name,slug,ratingSystem,avatar,coverImage")
    suspend fun getUserDetailsAsync(): Response<UserData>
}
