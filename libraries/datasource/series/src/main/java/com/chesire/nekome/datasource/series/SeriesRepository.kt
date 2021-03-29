package com.chesire.nekome.datasource.series

import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.datasource.series.remote.SeriesApi
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
     * Adds the anime series with id [seriesId] to the users tracked list.
     */
    suspend fun addAnime(seriesId: Int, startingStatus: UserSeriesStatus): Resource<SeriesDomain> {
        val idResult = userProvider.provideUserId()
        if (idResult !is UserProvider.UserIdResult.Success) {
            return Resource.Error.couldNotRefresh()
        }

        val response = seriesApi.addAnime(
            idResult.id,
            seriesId,
            startingStatus
        )
        return when (response) {
            is Resource.Success -> {
                val entity = map.toSeriesEntity(response.data)
                seriesDao.insert(entity)
                response
            }
            is Resource.Error -> {
                Timber.e("Error adding anime [$seriesId], ${response.msg}")
                response.mutate()
            }
        }
    }

    /**
     * Adds the manga series with id [seriesId] to the users tracked list.
     */
    suspend fun addManga(seriesId: Int, startingStatus: UserSeriesStatus): Resource<SeriesDomain> {
        val idResult = userProvider.provideUserId()
        if (idResult !is UserProvider.UserIdResult.Success) {
            return Resource.Error.couldNotRefresh()
        }

        val response = seriesApi.addManga(
            idResult.id,
            seriesId,
            startingStatus
        )
        return when (response) {
            is Resource.Success -> {
                val entity = map.toSeriesEntity(response.data)
                seriesDao.insert(entity)
                response
            }
            is Resource.Error -> {
                Timber.e("Error adding manga [$seriesId], ${response.msg}")
                response.mutate()
            }
        }
    }

    /**
     * Removes the series [seriesToRemove] from being tracked.
     */
    suspend fun deleteSeries(seriesToRemove: SeriesDomain): Resource<Any> {
        val response = seriesApi.delete(seriesToRemove.userId)
        when (response) {
            is Resource.Success -> seriesDao.delete(map.toSeriesEntity(seriesToRemove))
            is Resource.Error -> Timber.e(
                "Error deleting series [$seriesToRemove], ${response.msg}"
            )
        }

        return response
    }

    /**
     * Pulls and stores all of the users anime list.
     */
    suspend fun refreshAnime(): Resource<List<SeriesDomain>> {
        val idResult = userProvider.provideUserId()
        if (idResult !is UserProvider.UserIdResult.Success) {
            return Resource.Error.couldNotRefresh()
        }

        return when (val response = seriesApi.retrieveAnime(idResult.id)) {
            is Resource.Success -> {
                val entities = response.data.map { map.toSeriesEntity(it) }
                seriesDao.insert(entities)
                response
            }
            is Resource.Error -> {
                Timber.e("Error refreshing anime, ${response.msg}")
                response.mutate()
            }
        }
    }

    /**
     * Pulls and stores all of the users manga list.
     */
    suspend fun refreshManga(): Resource<List<SeriesDomain>> {
        val idResult = userProvider.provideUserId()
        if (idResult !is UserProvider.UserIdResult.Success) {
            return Resource.Error.couldNotRefresh()
        }

        return when (val response = seriesApi.retrieveManga(idResult.id)) {
            is Resource.Success -> {
                val entities = response.data.map { map.toSeriesEntity(it) }
                seriesDao.insert(entities)
                response
            }
            is Resource.Error -> {
                Timber.e("Error refreshing manga, ${response.msg}")
                response.mutate()
            }
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
    ): Resource<SeriesDomain> {
        return when (val response = seriesApi.update(userSeriesId, progress, status, rating)) {
            is Resource.Success -> {
                val entity = map.toSeriesEntity(response.data)
                seriesDao.update(entity)
                response
            }
            is Resource.Error -> {
                Timber.e("Error updating series [$userSeriesId], ${response.msg}")
                response.mutate()
            }
        }
    }
}
