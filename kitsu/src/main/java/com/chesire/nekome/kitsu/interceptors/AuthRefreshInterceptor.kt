package com.chesire.nekome.kitsu.interceptors

import com.chesire.nekome.kitsu.AuthProvider
import com.chesire.nekome.kitsu.api.auth.KitsuAuthService
import com.chesire.nekome.kitsu.api.auth.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Interceptor to handle refreshing access tokens if required.
 *
 * There is an official way to do this using OkHttp or Retrofit, but unfortunately this has to be
 * done as an interceptor instead as the official way expects a 401 to be returned if auth fails,
 * but Kitsu returns a 403 which won't work.
 *
 * If we still cannot refresh the token after attempting here, force a 401 to be returned back to
 * the calling layer and let that handle it.
 */
class AuthRefreshInterceptor @Inject constructor(
    private val provider: AuthProvider,
    private val auth: KitsuAuthService
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val response = chain.proceed(originRequest)

        return if (!response.isSuccessful && response.code() == 403) {
            val authResponse = runBlocking { getNewAuth(provider.refreshToken) }
            if (authResponse.isSuccessful && authResponse.body() != null) {
                authResponse.body()?.let {
                    provider.accessToken = it.accessToken
                    provider.refreshToken = it.refreshToken

                    chain.proceed(
                        originRequest.newBuilder()
                            .header("Authorization", "Bearer ${it.accessToken}")
                            .build()
                    )
                } ?: generateFailureResponse()
            } else {
                generateFailureResponse()
            }
        } else {
            response
        }
    }

    private suspend fun getNewAuth(refreshToken: String) =
        auth.refreshAccessTokenAsync(RefreshTokenRequest(refreshToken))

    private fun generateFailureResponse(): Response {
        // If there is an auth failure report a 401 error for the app to logout with
        return Response
            .Builder()
            .code(401)
            .build()
    }
}
