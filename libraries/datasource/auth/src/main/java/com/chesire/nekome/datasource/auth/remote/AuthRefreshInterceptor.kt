package com.chesire.nekome.datasource.auth.remote

import com.chesire.nekome.core.AuthCaster
import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.auth.AuthException
import com.chesire.nekome.datasource.auth.local.AuthProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject

/**
 * Interceptor to handle refreshing access tokens if required.
 *
 * There is an official way to do this using OkHttp or Retrofit, but unfortunately it has to be done
 * this way in an interceptor as some apis return a 403, and the official way only works if your api
 * returns a 403.
 *
 * If we still cannot refresh the token after attempting here, notify the [AuthCaster] so it can
 * tell any listeners of the failure.
 */
class AuthRefreshInterceptor @Inject constructor(
    private val provider: AuthProvider,
    private val auth: AuthApi,
    private val authCaster: AuthCaster
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val response = chain.proceed(originRequest)

        return if (!response.isSuccessful && response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
            val authResponse = runBlocking { auth.refresh() }
            if (authResponse is Resource.Success) {
                chain.proceed(
                    originRequest
                        .newBuilder()
                        .header("Authorization", "Bearer ${provider.accessToken}")
                        .build()
                )
            } else {
                authCaster.issueRefreshingToken()
                throw AuthException()
            }
        } else {
            response
        }
    }
}
