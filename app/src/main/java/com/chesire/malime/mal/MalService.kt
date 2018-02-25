package com.chesire.malime.mal

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MalService {
    @GET("anime/search.xml")
    fun searchForAnime(@Query("q") name: String): Call<Any>
}