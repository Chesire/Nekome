package com.chesire.malime.mal

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class MalApi(
        auth: String
) {
    private val SERVICE_ENDPOINT = "https://myanimelist.net/api/"
    private val malService: MalService

    init {
        val httpClient = OkHttpClient()
                .newBuilder()
                .addInterceptor(BasicAuthInterceptor(auth))
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(SERVICE_ENDPOINT)
                .client(httpClient)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()

        malService = retrofit.create(MalService::class.java)
    }

    fun loginToAccount(): Call<MalService.LoginToAccountResponse> {
        return malService.loginToAccount()
    }

    fun searchForAnime(name: String): Call<MalService.SearchForAnimeResponse> {
        return malService.searchForAnime(name)
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