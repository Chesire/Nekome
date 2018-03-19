package com.chesire.malime.mal

import com.chesire.malime.models.Anime
import com.chesire.malime.models.Entry
import com.chesire.malime.models.Manga
import com.chesire.malime.models.MyInfo
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MalService {
    @GET("malappinfo.php?&status=all&type=anime")
    fun getAllAnime(@Query("u") user: String): Call<GetAllAnimeResponse>

    @GET("malappinfo.php?&status=all&type=manga")
    fun getAllManga(@Query("u") user: String): Call<GetAllMangaResponse>

    @GET("api/account/verify_credentials.xml")
    fun loginToAccount(): Call<LoginToAccountResponse>

    @GET("api/anime/search.xml")
    fun searchForAnime(@Query("q") name: String): Call<SearchForAnimeResponse>

    @FormUrlEncoded
    @POST("api/animelist/update/{id}.xml")
    fun updateAnime(
        @Path("id") id: Int,
        @Field("data") data: String
    ): Call<Void>

    @Root(name = "myanimelist")
    data class GetAllAnimeResponse(
        @field:Element(name = "myinfo", required = false)
        @param:Element(name = "myinfo", required = false)
        val myInfo: MyInfo? = null,

        @field:ElementList(inline = true, entry = "anime")
        @param:ElementList(inline = true, entry = "anime")
        val animeList: List<Anime>
    )

    @Root(name = "myanimelist")
    data class GetAllMangaResponse(
        @field:Element(name = "myinfo", required = false)
        @param:Element(name = "myinfo", required = false)
        val myInfo: MyInfo? = null,

        @field:ElementList(inline = true, entry = "manga")
        @param:ElementList(inline = true, entry = "manga")
        val mangaList: List<Manga>
    )

    @Root(name = "user")
    data class LoginToAccountResponse(
        @field:Element(name = "id", required = false)
        @param:Element(name = "id", required = false)
        val id: Int? = null,

        @field:Element(name = "username", required = false)
        @param:Element(name = "username", required = false)
        val username: String? = null
    )

    @Root(name = "anime")
    data class SearchForAnimeResponse(
        @field:ElementList(inline = true, entry = "entry")
        @param:ElementList(inline = true, entry = "entry")
        val entries: List<Entry>
    )
}