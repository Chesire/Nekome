package com.chesire.nekome.datasource.auth.remote

import com.chesire.nekome.core.AuthCaster
import com.chesire.nekome.datasource.auth.AccessTokenRepository
import com.chesire.nekome.datasource.auth.AccessTokenResult
import com.chesire.nekome.datasource.auth.AuthException
import java.net.HttpURLConnection
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

/**
 * Interceptor to handle refreshing access tokens if required.
 *
 * There is an official way to do this using OkHttp or Retrofit, but unfortunately it has to be done
 * this way in an interceptor as some apis return a 403, and the official way only works if your api
 * returns a 401.
 *
 * If we still cannot refresh the token after attempting here, notify the [AuthCaster] so it can
 * tell any listeners of the failure.
 */
class AuthRefreshInterceptor @Inject constructor(
    private val repo: AccessTokenRepository,
    private val authCaster: AuthCaster
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        return if (response.isAuthError) {
            Timber.w("Response threw an auth error (${response.code()}), attempting to refresh")
            try {
                response.close()
            } catch (ex: Exception) {
                Timber.w(ex, "Unable to close response, carrying on")
            }
            val refreshResponse = runBlocking { repo.refresh() }
            if (refreshResponse is AccessTokenResult.Success) {
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .header("Authorization", "Bearer ${repo.accessToken}")
                        .build()
                )
            } else {
                Timber.w("Could not refresh the token, logging user out")
                authCaster.issueRefreshingToken()
                throw AuthException()
            }
        } else {
            response
        }
    }

    private val Response.isAuthError: Boolean
        get() = !isSuccessful &&
            code() == HttpURLConnection.HTTP_FORBIDDEN ||
            code() == HttpURLConnection.HTTP_UNAUTHORIZED
}
