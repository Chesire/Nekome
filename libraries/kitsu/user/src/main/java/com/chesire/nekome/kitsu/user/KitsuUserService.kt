package com.chesire.nekome.kitsu.user

import com.chesire.nekome.kitsu.user.dto.UserResponseDto
import retrofit2.Response
import retrofit2.http.GET

/**
 * Constructed using Retrofit to interface with the Kitsu API for queries related to a user.
 */
interface KitsuUserService {

    /**
     * Gets the details about a user.
     * This requires an auth token set in the header to get the correct user.
     */
    @GET("api/edge/users?filter[self]=true&fields[users]=id,name,avatar,coverImage")
    suspend fun getUserDetailsAsync(): Response<UserResponseDto>
}
