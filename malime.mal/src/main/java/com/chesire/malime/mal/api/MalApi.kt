package com.chesire.malime.mal.api

import com.chesire.malime.mal.BuildConfig
import com.chesire.malime.mal.models.response.GetAllAnimeResponse
import com.chesire.malime.mal.models.response.GetAllMangaResponse
import com.chesire.malime.mal.models.response.LoginToAccountResponse
import com.chesire.malime.mal.models.response.SearchForAnimeResponse
import com.chesire.malime.mal.models.response.SearchForMangaResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

internal const val MyAnimeListEndpoint = "https://myanimelist.net/"

/**
 * Provides the layer between the [MalService] and the [MalManager].
 * <p>
 * This generates the Retrofit instance to use with [MalService] and gives simple methods to execute
 * on it.
 */
class MalApi(
    auth: String
) {
    private val malService: MalService

    init {
        val httpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(BasicAuthInterceptor(auth))

        if (BuildConfig.DEBUG) {
            val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

            httpClient.addInterceptor(interceptor)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(MyAnimeListEndpoint)
            .client(httpClient.build())
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()

        malService = retrofit.create(MalService::class.java)
    }

    /**
     * Wraps [MalService.addAnime] method.
     */
    fun addAnime(id: Int, addAnimeXml: String): Call<Void> {
        return malService.addAnime(id, addAnimeXml)
    }

    /**
     * Wraps [MalService.addManga] method.
     */
    fun addManga(id: Int, addMangaXml: String): Call<Void> {
        return malService.addManga(id, addMangaXml)
    }

    /**
     * Wraps the [MalService.getAllAnime] method.
     */
    fun getAllAnime(username: String): Call<GetAllAnimeResponse> {
        return malService.getAllAnime(username)
    }

    /**
     * Wraps the [MalService.getAllManga] method.
     */
    fun getAllManga(username: String): Call<GetAllMangaResponse> {
        return malService.getAllManga(username)
    }

    /**
     * Wraps the [MalService.loginToAccount] method.
     */
    fun loginToAccount(): Call<LoginToAccountResponse> {
        return malService.loginToAccount()
    }

    /**
     * Wraps the [MalService.searchForAnime] method.
     */
    fun searchForAnime(name: String): Call<SearchForAnimeResponse> {
        return malService.searchForAnime(name)
    }

    /**
     * Wraps the [MalService.searchForManga] method.
     */
    fun searchForManga(name: String): Call<SearchForMangaResponse> {
        return malService.searchForManga(name)
    }

    /**
     * Wraps the [MalService.updateAnime] method.
     */
    fun updateAnime(id: Int, updateAnimeXml: String): Call<Void> {
        return malService.updateAnime(id, updateAnimeXml)
    }

    /**
     * Wraps the [MalService.updateManga] method.
     */
    fun updateManga(id: Int, updateMangaXml: String): Call<Void> {
        return malService.updateManga(id, updateMangaXml)
    }

    /**
     * Provides an interceptor that handles the basic auth.
     */
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