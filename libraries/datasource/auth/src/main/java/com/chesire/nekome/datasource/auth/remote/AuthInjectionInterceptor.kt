package com.chesire.nekome.datasource.auth.remote

import com.chesire.nekome.datasource.auth.AccessTokenRepository
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to push the authorization header into api requests.
 */
class AuthInjectionInterceptor @Inject constructor(
    private val repo: AccessTokenRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val authenticatedRequest = chain.request()
            .newBuilder()
            .header("Authorization", "Bearer ${repo.accessToken}")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
