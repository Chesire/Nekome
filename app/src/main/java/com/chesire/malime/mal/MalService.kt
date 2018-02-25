package com.chesire.malime.mal

import com.chesire.malime.models.Entry
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MalService {
    @GET("anime/search.xml")
    fun searchForAnime(@Query("q") name: String): Call<SearchForAnimeResponse>

    @Root(name = "anime")
    data class SearchForAnimeResponse(
            @field:ElementList(inline = true, entry = "entry")
            @param:ElementList(inline = true, entry = "entry")
            val entries: List<Entry>)
}