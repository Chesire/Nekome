package com.chesire.nekome.server.api

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.core.Resource

/**
 * Methods relating pulling or modifying the users library.
 */
interface LibraryApi {
    /**
     * Retrieves all of the users anime.
     */
    suspend fun retrieveAnime(userId: Int): Resource<List<SeriesModel>>

    /**
     * Retrieves all of the users manga.
     */
    suspend fun retrieveManga(userId: Int): Resource<List<SeriesModel>>

    /**
     * Adds the anime series with an id of [seriesId].
     */
    suspend fun addAnime(
        userId: Int,
        seriesId: Int,
        startingStatus: UserSeriesStatus
    ): Resource<SeriesModel>

    /**
     * Adds the manga series with an id of [seriesId].
     */
    suspend fun addManga(
        userId: Int,
        seriesId: Int,
        startingStatus: UserSeriesStatus
    ): Resource<SeriesModel>

    /**
     * Updates the state of a users series, passing in the users ID for the series.
     */
    suspend fun update(
        userSeriesId: Int,
        progress: Int,
        newStatus: UserSeriesStatus
    ): Resource<SeriesModel>

    /**
     * Deletes a series from the users library, passing in the users ID for the series.
     */
    suspend fun delete(userSeriesId: Int): Resource<Any>
}
