package com.chesire.malime.kitsu.api

import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.kitsu.BuildConfig
import com.chesire.malime.kitsu.models.request.LoginRequest
import com.chesire.malime.kitsu.models.response.AddItemResponse
import com.chesire.malime.kitsu.models.response.LibraryResponse
import com.chesire.malime.kitsu.models.response.LoginResponse
import com.chesire.malime.kitsu.models.response.UpdateItemResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal const val KitsuEndpoint = "https://kitsu.io/"

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
            .baseUrl(KitsuEndpoint)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        kitsuService = retrofit.create(KitsuService::class.java)
    }

    fun login(username: String, password: String): Call<LoginResponse> {
        return kitsuService.login(
            LoginRequest(
                username,
                password
            )
        )
    }

    fun getUser(): Call<LibraryResponse> {
        return kitsuService.getUser()
    }

    fun getUserLibrary(userId: Int, offset: Int): Call<LibraryResponse> {
        return kitsuService.getUserLibrary(userId, offset)
    }

    fun search(title: String, type: ItemType): Call<LibraryResponse> {
        return kitsuService.search(type.text, title)
    }

    fun addItem(data: RequestBody): Call<AddItemResponse> {
        return kitsuService.addItem(data)
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
                .build()

            return chain.proceed(authenticatedRequest)
        }
    }
}