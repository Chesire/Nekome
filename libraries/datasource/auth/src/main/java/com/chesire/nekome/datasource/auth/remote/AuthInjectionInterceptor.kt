package com.chesire.nekome.datasource.auth.remote

import com.chesire.nekome.datasource.auth.local.AuthProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Interceptor to push the authorization header into api requests.
 */
class AuthInjectionInterceptor @Inject constructor(
    private val provider: AuthProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val authenticatedRequest = chain.request()
            .newBuilder()
            .header("Authorization", "Bearer ${provider.accessToken}")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
