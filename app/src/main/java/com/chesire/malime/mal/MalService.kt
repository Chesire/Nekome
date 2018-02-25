package com.chesire.malime.mal

import com.chesire.malime.models.Anime
import com.chesire.malime.models.Entry
import org.simpleframework.xml.Root
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MalService {
    @GET("anime/search.xml")
    fun searchForAnime(@Query("q") name: String): Call<Anime>

    //data class SearchForAnimeResponse(val anime: Anime)
}