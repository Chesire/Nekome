package com.chesire.nekome.kitsu.api.user

import com.chesire.nekome.core.models.UserModel
import retrofit2.Response
import retrofit2.http.GET

/**
 * Constructed using Retrofit to interface with the Kitsu API for queries related to getting user
 * details.
 */
interface KitsuUserService {
    /**
     * Gets the user details, requires authentication to be set.
     */
    @GET("api/edge/users?filter[self]=true&fields[users]=id,name,slug,ratingSystem,avatar,coverImage")
    suspend fun getUserDetailsAsync(): Response<UserModel>
}
