package com.chesire.nekome.kitsu.interceptors

import com.chesire.nekome.kitsu.AuthProvider
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.AuthApi
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
    private val auth: AuthApi
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val response = chain.proceed(originRequest)

        return if (!response.isSuccessful && response.code() == 403) {
            val authResponse = runBlocking { auth.refresh() }
            if (authResponse is Resource.Success) {
                chain.proceed(
                    originRequest
                        .newBuilder()
                        .header("Authorization", "Bearer ${provider.accessToken}")
                        .build()
                )
            } else {
                generateFailureResponse(response)
            }
        } else {
            response
        }
    }

    private fun generateFailureResponse(originResponse: Response): Response {
        // If there is an unrecoverable auth failure report a 401 error for the app to logout with
        return Response
            .Builder()
            .request(originResponse.request())
            .protocol(originResponse.protocol())
            .code(401)
            .message(originResponse.message())
            .build()
    }
}
