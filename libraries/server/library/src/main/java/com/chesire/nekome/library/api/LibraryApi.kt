package com.chesire.nekome.library.api

import com.chesire.nekome.core.Resource
import com.chesire.nekome.seriesflags.UserSeriesStatus

/**
 * Methods relating to interacting with the users library.
 */
interface LibraryApi {
    /**
     * Retrieves all of the users anime.
     */
    suspend fun retrieveAnime(userId: Int): Resource<List<LibraryDomain>>

    /**
     * Retrieves all of the users manga.
     */
    suspend fun retrieveManga(userId: Int): Resource<List<LibraryDomain>>

    /**
     * Adds the anime series with an id of [seriesId].
     */
    suspend fun addAnime(
        userId: Int,
        seriesId: Int,
        startingStatus: UserSeriesStatus
    ): Resource<LibraryDomain>

    /**
     * Adds the manga series with an id of [seriesId].
     */
    suspend fun addManga(
        userId: Int,
        seriesId: Int,
        startingStatus: UserSeriesStatus
    ): Resource<LibraryDomain>

    /**
     * Updates the state of a users series, passing in the users ID for the series.
     */
    suspend fun update(
        userSeriesId: Int,
        progress: Int,
        newStatus: UserSeriesStatus
    ): Resource<LibraryDomain>

    /**
     * Deletes a series from the users library, passing in the users ID for the series.
     */
    suspend fun delete(userSeriesId: Int): Resource<Any>
}
