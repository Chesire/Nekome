package com.chesire.nekome.datasource.series.remote

import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.datasource.series.SeriesDomain

/**
 * Methods relating to interacting with the users library.
 */
interface SeriesApi {

    /**
     * Retrieves all of the users anime.
     */
    suspend fun retrieveAnime(userId: Int): Resource<List<SeriesDomain>>

    /**
     * Retrieves all of the users manga.
     */
    suspend fun retrieveManga(userId: Int): Resource<List<SeriesDomain>>

    /**
     * Adds the anime series with an id of [seriesId].
     */
    suspend fun addAnime(
        userId: Int,
        seriesId: Int,
        startingStatus: UserSeriesStatus
    ): Resource<SeriesDomain>

    /**
     * Adds the manga series with an id of [seriesId].
     */
    suspend fun addManga(
        userId: Int,
        seriesId: Int,
        startingStatus: UserSeriesStatus
    ): Resource<SeriesDomain>

    /**
     * Updates the state of a users series, passing in the users ID for the series.
     */
    suspend fun update(
        userSeriesId: Int,
        progress: Int,
        newStatus: UserSeriesStatus,
        rating: Int
    ): Resource<SeriesDomain>

    /**
     * Deletes a series from the users library, passing in the users ID for the series.
     */
    suspend fun delete(userSeriesId: Int): Resource<Any>
}
