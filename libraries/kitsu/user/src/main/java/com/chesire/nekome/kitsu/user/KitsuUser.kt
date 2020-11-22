package com.chesire.nekome.kitsu.user

import com.chesire.nekome.core.EntityMapper
import com.chesire.nekome.core.Resource
import com.chesire.nekome.kitsu.asError
import com.chesire.nekome.kitsu.parse
import com.chesire.nekome.user.api.UserApi
import com.chesire.nekome.user.api.UserEntity
import retrofit2.Response
import javax.inject.Inject

/**
 * Implementation of the [UserApi] for usage with the Kitsu API.
 */
class KitsuUser @Inject constructor(
    private val userService: KitsuUserService,
    private val map: EntityMapper<KitsuUserEntity, UserEntity>
) : UserApi {

    override suspend fun getUserDetails(): Resource<UserEntity> {
        return try {
            parseResponse(userService.getUserDetailsAsync())
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    private fun parseResponse(response: Response<UserData>): Resource<UserEntity> {
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
