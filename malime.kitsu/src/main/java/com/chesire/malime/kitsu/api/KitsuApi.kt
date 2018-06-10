package com.chesire.malime.kitsu.api

import com.chesire.malime.kitsu.BuildConfig
import com.chesire.malime.kitsu.models.LibraryResponse
import com.chesire.malime.kitsu.models.LoginRequest
import com.chesire.malime.kitsu.models.LoginResponse
import com.chesire.malime.kitsu.models.UpdateItemResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
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

    fun getUser(username: String): Call<LibraryResponse> {
        return kitsuService.getUser(username)
    }

    fun getUserLibrary(userId: Int, offset: Int): Call<LibraryResponse> {
        return kitsuService.getUserLibrary(userId, offset)
    }

    fun updateItem(seriesId: Int, updateModel: RequestBody): Call<UpdateItemResponse> {
        return kitsuService.updateItem(seriesId, updateModel)
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
                .header(
                    "client_id",
                    "dd031b32d2f56c990b1425efe6c42ad847e7fe3ab46bf1299f05ecd856bdb7dd"
                )
                .build()

            return chain.proceed(authenticatedRequest)
        }
    }
}