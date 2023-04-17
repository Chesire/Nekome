package com.chesire.nekome.datasource.series

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ErrorDomain
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.datasource.series.remote.SeriesApi
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.flow.map
import timber.log.Timber

/**
 * Repository to interact with a users list of series.
 */
class SeriesRepository(
    private val seriesDao: SeriesDao,
    private val seriesApi: SeriesApi,
    private val userProvider: UserProvider,
    private val map: SeriesMapper
) {

    /**
     * Observable list of all the users series (Anime + Manga).
     */
    fun getSeries() = seriesDao.getSeries().map { it.map { map.toSeriesDomain(it) } }

    /**
     * Gets a single [SeriesDomain] which has a matching [userSeriesId].
     */
    suspend fun getSeries(userSeriesId: Int): SeriesDomain =
        map.toSeriesDomain(seriesDao.getSeries(userSeriesId))

    /**
     * Adds the anime series with id [seriesId] to the users tracked list.
     */
    suspend fun addAnime(
        seriesId: Int,
        startingStatus: UserSeriesStatus
    ): Result<SeriesDomain, ErrorDomain> {
        val idResult = userProvider.provideUserId()
        if (idResult !is UserProvider.UserIdResult.Success) {
            return Err(ErrorDomain.couldNotRefresh)
        }

        return seriesApi.addAnime(
            userId = idResult.id,
            seriesId = seriesId,
            startingStatus = startingStatus
        ).onSuccess {
            val entity = map.toSeriesEntity(it)
            seriesDao.insert(entity)
        }.onFailure {
            Timber.e("Error adding anime [$seriesId], ${it.message}")
        }
    }

    /**
     * Adds the manga series with id [seriesId] to the users tracked list.
     */
    suspend fun addManga(
        seriesId: Int,
        startingStatus: UserSeriesStatus
    ): Result<SeriesDomain, ErrorDomain> {
        val idResult = userProvider.provideUserId()
        if (idResult !is UserProvider.UserIdResult.Success) {
            return Err(ErrorDomain.couldNotRefresh)
        }

        return seriesApi.addManga(
            userId = idResult.id,
            seriesId = seriesId,
            startingStatus = startingStatus
        ).onSuccess {
            val entity = map.toSeriesEntity(it)
            seriesDao.insert(entity)
        }.onFailure {
            Timber.e("Error adding anime [$seriesId], ${it.message}")
        }
    }

    /**
     * Removes the series [seriesToRemove] from being tracked.
     */
    suspend fun deleteSeries(seriesToRemove: SeriesDomain): Result<Unit, ErrorDomain> {
        return seriesApi.delete(seriesToRemove.userId)
            .onSuccess {
                seriesDao.delete(map.toSeriesEntity(seriesToRemove))
            }
            .onFailure {
                Timber.e("Error deleting series [$seriesToRemove], ${it.message}")
            }
    }

    /**
     * Pulls and stores all of the users anime list.
     */
    suspend fun refreshAnime(): Result<List<SeriesDomain>, ErrorDomain> {
        val idResult = userProvider.provideUserId()
        if (idResult !is UserProvider.UserIdResult.Success) {
            return Err(ErrorDomain.couldNotRefresh)
        }

        return seriesApi.retrieveAnime(idResult.id)
            .onSuccess {
                val entities = it.map { map.toSeriesEntity(it) }
                seriesDao.insert(entities)
            }
            .onFailure {
                Timber.e("Error refreshing anime, ${it.message}")
            }
    }

    /**
     * Pulls and stores all of the users manga list.
     */
    suspend fun refreshManga(): Result<List<SeriesDomain>, ErrorDomain> {
        val idResult = userProvider.provideUserId()
        if (idResult !is UserProvider.UserIdResult.Success) {
            return Err(ErrorDomain.couldNotRefresh)
        }

        return seriesApi.retrieveManga(idResult.id)
            .onSuccess {
                val entities = it.map { map.toSeriesEntity(it) }
                seriesDao.insert(entities)
            }
            .onFailure {
                Timber.e("Error refreshing manga, ${it.message}")
            }
    }

    /**
     * Updates the stored data about a users series, mapped via the users id for the series
     * [userSeriesId].
     */
    suspend fun updateSeries(
        userSeriesId: Int,
        progress: Int,
        status: UserSeriesStatus,
        rating: Int
    ): Result<SeriesDomain, ErrorDomain> {
        return seriesApi.update(
            userSeriesId = userSeriesId,
            progress = progress,
            newStatus = status,
            rating = rating
        ).onSuccess {
            val entity = map.toSeriesEntity(it)
            seriesDao.update(entity)
        }.onFailure {
            Timber.e("Error updating series [$userSeriesId], ${it.message}")
        }
    }
}
