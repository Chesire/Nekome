package com.chesire.nekome.kitsu.interceptors

import com.chesire.nekome.kitsu.AuthProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

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
