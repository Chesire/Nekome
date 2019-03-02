package com.chesire.malime.kitsu.api.auth

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.kitsu.AuthProvider
import com.chesire.malime.kitsu.emptyResponseError
import com.chesire.malime.kitsu.parse
import javax.inject.Inject

class KitsuAuth @Inject constructor(
    private val authService: KitsuAuthService,
    private val authProvider: AuthProvider
) : AuthApi {
    override suspend fun login(username: String, password: String): Resource<Any> {
        return try {
            val response = authService.loginAsync(LoginRequest(username, password)).await()
            return if (response.isSuccessful) {
                response.body()?.let { responseObject ->
                    authProvider.apply {
                        accessToken = responseObject.accessToken
                        refreshToken = responseObject.refreshToken
                    }
                    Resource.Success(Any())
                } ?: emptyResponseError()
            } else {
                Resource.Error(
                    response.errorBody()?.string() ?: response.message(),
                    response.code()
                )
            }
        } catch (ex: Exception) {
            ex.parse()
        }
    }
}
