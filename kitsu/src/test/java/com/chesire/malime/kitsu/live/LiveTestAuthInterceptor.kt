package com.chesire.malime.kitsu.live

import okhttp3.Interceptor
import okhttp3.Response

class LiveTestAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // DO NOT COMMIT THE AUTH VALUE
        val authenticatedRequest = request.newBuilder()
            .header(
                "Authorization",
                "Bearer INSERT AUTH TOKEN"
            )
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
