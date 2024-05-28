package com.chesire.nekome.datasource.series.remote

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ErrorDomain
import com.chesire.nekome.datasource.series.SeriesDomain
import com.github.michaelbull.result.Result

/**
 * Methods relating to interacting with the users library.
 */
interface SeriesApi {

    /**
     * Retrieves all of the users anime.
     */
    suspend fun retrieveAnime(userId: Int): Result<List<SeriesDomain>, ErrorDomain>

    /**
     * Retrieves all of the users manga.
     */
    suspend fun retrieveManga(userId: Int): Result<List<SeriesDomain>, ErrorDomain>

    /**
     * Adds the anime series with an id of [seriesId].
     */
    suspend fun addAnime(
        userId: Int,
        seriesId: Int,
        startingStatus: UserSeriesStatus
    ): Result<SeriesDomain, ErrorDomain>

    /**
     * Adds the manga series with an id of [seriesId].
     */
    suspend fun addManga(
        userId: Int,
        seriesId: Int,
        startingStatus: UserSeriesStatus
    ): Result<SeriesDomain, ErrorDomain>

    /**
     * Updates the state of a users series, passing in the users ID for the series.
     */
    suspend fun update(
        userSeriesId: Int,
        progress: Int,
        volumesOwned: Int?,
        newStatus: UserSeriesStatus,
        rating: Int
    ): Result<SeriesDomain, ErrorDomain>

    /**
     * Deletes a series from the users library, passing in the users ID for the series.
     */
    suspend fun delete(userSeriesId: Int): Result<Unit, ErrorDomain>
}
