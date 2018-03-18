package com.chesire.malime.mal

import com.chesire.malime.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class MalApi(
        auth: String
) {
    private val SERVICE_ENDPOINT = "https://myanimelist.net/"
    private val malService: MalService

    init {
        // This should be stripped out on release
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        val httpClient = OkHttpClient()
                .newBuilder()
                .addInterceptor(BasicAuthInterceptor(auth))
                .addInterceptor(interceptor)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(SERVICE_ENDPOINT)
                .client(httpClient)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()

        malService = retrofit.create(MalService::class.java)
    }

    fun getAllAnime(username: String): Call<MalService.GetAllAnimeResponse> {
        return malService.getAllAnime(username)
    }

    fun getAllManga(username: String): Call<MalService.GetAllMangaResponse> {
        return malService.getAllManga(username)
    }

    fun loginToAccount(): Call<MalService.LoginToAccountResponse> {
        return malService.loginToAccount()
    }

    fun searchForAnime(name: String): Call<MalService.SearchForAnimeResponse> {
        return malService.searchForAnime(name)
    }

    fun updateAnime(id: Int, updateAnimeXml: String): Call<Void> {
        return malService.updateAnime(id, updateAnimeXml)
    }

    class BasicAuthInterceptor(
            private val auth: String
    ) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val authenticatedRequest = request.newBuilder()
                    .header("Authorization", "Basic $auth")
                    .build()

            return chain.proceed(authenticatedRequest)
        }
    }
}