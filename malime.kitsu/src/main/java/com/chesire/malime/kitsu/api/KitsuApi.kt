package com.chesire.malime.kitsu.api

import com.chesire.malime.kitsu.BuildConfig
import com.chesire.malime.kitsu.models.LoginRequest
import com.chesire.malime.kitsu.models.LoginResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val ENDPOINT = "https://kitsu.io/"

class KitsuApi(
    accessToken: String
) {
    private val kitsuService: KitsuService

    init {
        val httpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(BasicAuthInterceptor(accessToken))

        if (BuildConfig.DEBUG) {
            val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

            httpClient.addInterceptor(interceptor)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        kitsuService = retrofit.create(KitsuService::class.java)
    }

    fun login(username: String, password: String): Call<LoginResponse> {
        return kitsuService.login(LoginRequest(username, password))
    }

    /**
     * Provides an interceptor that handles the basic auth.
     */
    class BasicAuthInterceptor(
        private val accessToken: String
    ) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val authenticatedRequest = request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()

            return chain.proceed(authenticatedRequest)
        }
    }
}