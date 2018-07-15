package com.chesire.malime.kitsu.api

import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.kitsu.BuildConfig
import com.chesire.malime.kitsu.models.request.LoginRequest
import com.chesire.malime.kitsu.models.request.RefreshAuthRequest
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
import javax.inject.Inject

internal const val KitsuEndpoint = "https://kitsu.io/"

class KitsuApi @Inject constructor(authorizer: KitsuAuthorizer) {
    private val kitsuService: KitsuService

    init {
        val httpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(AuthInterceptor(authorizer))

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
        return kitsuService.login(LoginRequest(username, password))
    }

    fun refreshAuthToken(refreshToken: String): Call<LoginResponse> {
        return kitsuService.refreshAuth(RefreshAuthRequest(refreshToken))
    }

    fun getUser(): Call<LibraryResponse> {
        return kitsuService.getUser()
    }

    fun getUserLibrary(userId: Int, offset: Int, type: ItemType): Call<LibraryResponse> {
        // Ideally wanted to keep the api without logic, but this is nicer than in the manager
        return if (type == ItemType.Anime) {
            kitsuService.getUserAnime(userId, offset)
        } else {
            kitsuService.getUserManga(userId, offset)
        }
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
     * Provides an interceptor that handles the auth and refreshing the token when needed.
     */
    inner class AuthInterceptor(private val authorizer: KitsuAuthorizer) : Interceptor {
        private var updatingAuthToken = false

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            if (updatingAuthToken) {
                // If we are currently updating the token just push it through
                return chain.proceed(request)
            }

            val authModel = authorizer.retrieveAuthDetails()
            if (authModel.expireAt != 0L && System.currentTimeMillis() / 1000 > authModel.expireAt) {
                updateAuthToken(authModel)
            }

            val authenticatedRequest = request.newBuilder()
                .header("Authorization", "Bearer ${authModel.authToken}")
                .build()

            return chain.proceed(authenticatedRequest)
        }

        private fun updateAuthToken(authModel: AuthModel) {
            // The auth token has expired, update it using the refresh token
            updatingAuthToken = true
            val refreshCall = refreshAuthToken(authModel.refreshToken)
            val refreshResponse = refreshCall.execute()
            updatingAuthToken = false

            if (refreshResponse.isSuccessful) {
                refreshResponse.body().let {
                    authModel.authToken = it!!.accessToken
                    authModel.refreshToken = it.refreshToken
                    authModel.expireAt = it.createdAt + it.expiresIn
                }
                authorizer.storeAuthDetails(authModel)
            }
        }
    }
}