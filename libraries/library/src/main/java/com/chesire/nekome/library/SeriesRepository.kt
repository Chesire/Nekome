package com.chesire.nekome.library

import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.library.api.LibraryApi
import timber.log.Timber

/**
 * Repository to interact with a users list of series.
 */
class SeriesRepository(
    private val seriesDao: SeriesDao,
    private val libraryApi: LibraryApi,
    private val userProvider: UserProvider,
    private val map: LibraryDomainMapper
) {

    /**
     * Observable list of all the users series (Anime + Manga).
     */
    fun getSeries() = seriesDao.getSeries()

    /**
     * Adds the anime series with id [seriesId] to the users tracked list.
     */
    suspend fun addAnime(seriesId: Int, startingStatus: UserSeriesStatus): Resource<SeriesModel> {
        val response = libraryApi.addAnime(
            userProvider.provideUserId(),
            seriesId,
            startingStatus
        )
        return when (response) {
            is Resource.Success -> {
                val newData = map.toSeriesModel(response.data)
                seriesDao.insert(newData)
                response.with(newData)
            }
            is Resource.Error -> {
                Timber.e("Error adding anime [$seriesId], ${response.msg}")
                response.morph()
            }
        }
    }

    /**
     * Adds the manga series with id [seriesId] to the users tracked list.
     */
    suspend fun addManga(seriesId: Int, startingStatus: UserSeriesStatus): Resource<SeriesModel> {
        val response = libraryApi.addManga(
            userProvider.provideUserId(),
            seriesId,
            startingStatus
        )
        return when (response) {
            is Resource.Success -> {
                val newData = map.toSeriesModel(response.data)
                seriesDao.insert(newData)
                response.with(newData)
            }
            is Resource.Error -> {
                Timber.e("Error adding manga [$seriesId], ${response.msg}")
                response.morph()
            }
        }
    }

    /**
     * Removes the series [seriesToRemove] from being tracked.
     */
    suspend fun deleteSeries(seriesToRemove: SeriesModel): Resource<Any> {
        val response = libraryApi.delete(seriesToRemove.userId)
        when (response) {
            is Resource.Success -> seriesDao.delete(seriesToRemove)
            is Resource.Error -> Timber.e(
                "Error deleting series [$seriesToRemove], ${response.msg}"
            )
        }

        return response
    }

    /**
     * Pulls and stores all of the users anime list.
     */
    suspend fun refreshAnime(): Resource<List<SeriesModel>> {
        val response = libraryApi.retrieveAnime(userProvider.provideUserId())
        return when (response) {
            is Resource.Success -> {
                val add = response.data.map { map.toSeriesModel(it) }
                seriesDao.insert(add)
                response.with(add)
            }
            is Resource.Error -> {
                Timber.e("Error refreshing anime, ${response.msg}")
                response.morph()
            }
        }
    }

    /**
     * Pulls and stores all of the users manga list.
     */
    suspend fun refreshManga(): Resource<List<SeriesModel>> {
        val response = libraryApi.retrieveManga(userProvider.provideUserId())
        return when (response) {
            is Resource.Success -> {
                val add = response.data.map { map.toSeriesModel(it) }
                seriesDao.insert(add)
                response.with(add)
            }
            is Resource.Error -> {
                Timber.e("Error refreshing manga, ${response.msg}")
                response.morph()
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
        status: UserSeriesStatus
    ): Resource<SeriesModel> {
        val response = libraryApi.update(userSeriesId, progress, status)
        return when (response) {
            is Resource.Success -> {
                val add = map.toSeriesModel(response.data)
                seriesDao.update(add)
                response.with(add)
            }
            is Resource.Error -> {
                Timber.e("Error updating series [$userSeriesId], ${response.msg}")
                response.morph()
            }
        }
    }
}
