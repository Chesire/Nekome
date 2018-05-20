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

/**
 * Provides a Retrofit service to interact with the MyAnimeList API.
 */
interface MalService {
    /**
     * Executes a GET method to add an anime for the user.
     *
     * @param id The id of the anime to add, this will be the [Anime.seriesAnimeDbId]
     * @param data String representation of the anime model
     * @return Call to execute for the POST method
     */
    @FormUrlEncoded
    @POST("api/animelist/add/{id}.xml")
    fun addAnime(
        @Path("id") id: Int,
        @Field("data") data: String
    ): Call<Void>

    /**
     * Executes a GET method to add a manga for the user.
     *
     * @param id The id of the manga to add, this will be the [Manga.seriesMangaDbId]
     * @param data String representation of the manga model
     * @return Call to execute for the POST method
     */
    @FormUrlEncoded
    @POST("api/mangalist/add/{id}.xml")
    fun addManga(
        @Path("id") id: Int,
        @Field("data") data: String
    ): Call<Void>

    /**
     * Executes a GET to find all the anime for [user].
     *
     * @param user Username for the person to get the anime for
     * @return Call to execute for the GET method
     */
    @GET("malappinfo.php?&status=all&type=anime")
    fun getAllAnime(@Query("u") user: String): Call<GetAllAnimeResponse>

    /**
     * Executes a GET to find all the manga for [user].
     *
     * @param user Username for the person to get the manga for
     * @return Call to execute for the GET method
     */
    @GET("malappinfo.php?&status=all&type=manga")
    fun getAllManga(@Query("u") user: String): Call<GetAllMangaResponse>

    /**
     * Executes a login action to ensure the user credentials are correct.
     *
     * User credentials are created on initialization of the Retrofit interface in [MalApi].
     *
     * @return Call to execute for the GET method
     */
    @GET("api/account/verify_credentials.xml")
    fun loginToAccount(): Call<LoginToAccountResponse>

    /**
     * Executes a GET to search for the specified anime.
     *
     * @param name Name of the anime to find
     * @return Call to execute for the GET method
     */
    @GET("api/anime/search.xml")
    fun searchForAnime(@Query("q") name: String): Call<SearchForAnimeResponse>

    /**
     * Executes a GET to search for the specified manga.
     *
     * @param name Name of the manga to find
     * @return Call to execute for the GET method
     */
    @GET("api/manga/search.xml")
    fun searchForManga(@Query("q") name: String): Call<SearchForMangaResponse>

    /**
     * Executes a POST to update the data for an anime.
     *
     * The [data] param must be a string representation of XML.
     *
     * @param id The id of the anime to update, this will be the [Anime.seriesAnimeDbId]
     * @param data String representation of the anime model, handled by the [UpdateAnime.getXml]
     * @return Call to execute for the POST method
     */
    @FormUrlEncoded
    @POST("api/animelist/update/{id}.xml")
    fun updateAnime(
        @Path("id") id: Int,
        @Field("data") data: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("api/mangalist/update/{id}.xml")
    fun updateManga(
        @Path("id") id: Int,
        @Field("data") data: String
    ): Call<Void>

    /**
     * Response object to handle calls to [getAllAnime].
     */
    @Root(name = "myanimelist")
    data class GetAllAnimeResponse(
        @field:Element(name = "myinfo", required = false)
        @param:Element(name = "myinfo", required = false)
        val myInfo: MyInfo? = null,

        @field:ElementList(inline = true, entry = "anime", required = false)
        @param:ElementList(inline = true, entry = "anime", required = false)
        val animeList: List<Anime>? = null
    )

    /**
     * Response object to handle calls to [getAllManga].
     */
    @Root(name = "myanimelist")
    data class GetAllMangaResponse(
        @field:Element(name = "myinfo", required = false)
        @param:Element(name = "myinfo", required = false)
        val myInfo: MyInfo? = null,

        @field:ElementList(inline = true, entry = "manga", required = false)
        @param:ElementList(inline = true, entry = "manga", required = false)
        val mangaList: List<Manga>? = null
    )

    /**
     * Response object to handle calls to [loginToAccount].
     */
    @Root(name = "user")
    data class LoginToAccountResponse(
        @field:Element(name = "id", required = false)
        @param:Element(name = "id", required = false)
        val id: Int? = null,

        @field:Element(name = "username", required = false)
        @param:Element(name = "username", required = false)
        val username: String? = null
    )

    /**
     * Response object to handle calls to [searchForAnime].
     */
    @Root(name = "anime")
    data class SearchForAnimeResponse(
        @field:ElementList(inline = true, entry = "entry")
        @param:ElementList(inline = true, entry = "entry")
        val entries: List<Entry>
    )

    /**
     * Response object to handle calls to [searchForManga].
     */
    @Root(name = "manga")
    data class SearchForMangaResponse(
        @field:ElementList(inline = true, entry = "entry")
        @param:ElementList(inline = true, entry = "entry")
        val entries: List<Entry>
    )
}