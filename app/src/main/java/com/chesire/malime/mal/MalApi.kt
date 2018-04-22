package com.chesire.malime.mal

import com.chesire.malime.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

/**
 * Provides the layer between the [MalService] and the [MalManager].
 * <p>
 * This generates the Retrofit instance to use with [MalService] and gives simple methods to execute
 * on it.
 */
class MalApi(
    auth: String
) {
    private val SERVICE_ENDPOINT = "https://myanimelist.net/"
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
            .baseUrl(SERVICE_ENDPOINT)
            .client(httpClient.build())
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()

        malService = retrofit.create(MalService::class.java)
    }

    /**
     * Wraps the [MalService.getAllAnime] method.
     */
    fun getAllAnime(username: String): Call<MalService.GetAllAnimeResponse> {
        return malService.getAllAnime(username)
    }

    /**
     * Wraps the [MalService.getAllManga] method.
     */
    fun getAllManga(username: String): Call<MalService.GetAllMangaResponse> {
        return malService.getAllManga(username)
    }

    /**
     * Wraps the [MalService.loginToAccount] method.
     */
    fun loginToAccount(): Call<MalService.LoginToAccountResponse> {
        return malService.loginToAccount()
    }

    /**
     * Wraps the [MalService.searchForAnime] method.
     */
    fun searchForAnime(name: String): Call<MalService.SearchForAnimeResponse> {
        return malService.searchForAnime(name)
    }

    /**
     * Wraps the [MalService.searchForManga] method.
     */
    fun searchForManga(name: String): Call<MalService.SearchForMangaResponse> {
        return malService.searchForManga(name)
    }

    /**
     * Wraps the [MalService.updateAnime] method.
     */
    fun updateAnime(id: Int, updateAnimeXml: String): Call<Void> {
        return malService.updateAnime(id, updateAnimeXml)
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