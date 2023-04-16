package com.chesire.nekome.kitsu.user

import com.chesire.nekome.core.models.ErrorDomain
import com.chesire.nekome.datasource.user.UserDomain
import com.chesire.nekome.datasource.user.remote.UserApi
import com.chesire.nekome.kitsu.asError
import com.chesire.nekome.kitsu.parse
import com.chesire.nekome.kitsu.user.dto.UserResponseDto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import javax.inject.Inject
import retrofit2.Response

/**
 * Implementation of the [UserApi] for usage with the Kitsu API.
 */
@Suppress("TooGenericExceptionCaught")
class KitsuUser @Inject constructor(
    private val userService: KitsuUserService,
    private val map: UserItemDtoMapper
) : UserApi {

    override suspend fun getUserDetails(): Result<UserDomain, ErrorDomain> {
        return try {
            parseResponse(userService.getUserDetailsAsync())
        } catch (ex: Exception) {
            Err(ex.parse())
        }
    }

    private fun parseResponse(response: Response<UserResponseDto>): Result<UserDomain, ErrorDomain> {
        return if (response.isSuccessful) {
            response.body()
                ?.data
                ?.firstOrNull()
                ?.let { user ->
                    Ok(map.toUserDomain(user))
                } ?: Err(ErrorDomain.emptyResponse)
        } else {
            Err(response.asError())
        }
    }
}
