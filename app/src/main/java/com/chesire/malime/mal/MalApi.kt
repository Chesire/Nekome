package com.chesire.malime.mal

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class MalApi {
    private val SERVICE_ENDPOINT = "https://myanimelist.net/api"
    private val mMalService: MalService

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(SERVICE_ENDPOINT)
                .client(OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()

        mMalService = retrofit.create(MalService::class.java)
    }

    fun searchForAnime(name: String): Call<Any> {
        return mMalService.searchForAnime(name)
    }
}