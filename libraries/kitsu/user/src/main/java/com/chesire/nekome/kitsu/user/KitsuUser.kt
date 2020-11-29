package com.chesire.nekome.kitsu.user

import com.chesire.nekome.core.Resource
import com.chesire.nekome.kitsu.asError
import com.chesire.nekome.kitsu.parse
import com.chesire.nekome.kitsu.user.dto.UserResponseDto
import com.chesire.nekome.user.api.UserApi
import com.chesire.nekome.user.api.UserDomain
import retrofit2.Response
import javax.inject.Inject

/**
 * Implementation of the [UserApi] for usage with the Kitsu API.
 */
@Suppress("TooGenericExceptionCaught")
class KitsuUser @Inject constructor(
    private val userService: KitsuUserService,
    private val map: KitsuUserDtoMapper
) : UserApi {

    override suspend fun getUserDetails(): Resource<UserDomain> {
        return try {
            parseResponse(userService.getUserDetailsAsync())
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    private fun parseResponse(response: Response<UserResponseDto>): Resource<UserDomain> {
        return if (response.isSuccessful) {
            response.body()
                ?.data
                ?.firstOrNull()
                ?.let { user ->
                    Resource.Success(map.from(user))
                } ?: Resource.Error.emptyResponse()
        } else {
            response.asError()
        }
    }
}
