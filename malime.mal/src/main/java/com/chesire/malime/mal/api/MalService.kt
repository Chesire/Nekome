package com.chesire.malime.mal.api

import com.chesire.malime.mal.models.Anime
import com.chesire.malime.mal.models.Manga
import com.chesire.malime.mal.models.response.GetAllAnimeResponse
import com.chesire.malime.mal.models.response.GetAllMangaResponse
import com.chesire.malime.mal.models.response.LoginToAccountResponse
import com.chesire.malime.mal.models.response.SearchForAnimeResponse
import com.chesire.malime.mal.models.response.SearchForMangaResponse
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
     * @param data String representation of the anime model
     * @return Call to execute for the POST method
     */
    @FormUrlEncoded
    @POST("api/animelist/update/{id}.xml")
    fun updateAnime(
        @Path("id") id: Int,
        @Field("data") data: String
    ): Call<Void>

    /**
     * Executes a POST to update the data for a manga.
     *
     * The [data] param must be a string representation of XML.
     *
     * @param id The id of the manga to update, this will be the [Manga.seriesMangaDbId]
     * @param data String representation of the manga model
     * @return Call to execute for the POST method
     */
    @FormUrlEncoded
    @POST("api/mangalist/update/{id}.xml")
    fun updateManga(
        @Path("id") id: Int,
        @Field("data") data: String
    ): Call<Void>
}