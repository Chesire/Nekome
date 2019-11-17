package com.chesire.malime.series

import androidx.lifecycle.LiveData
import com.chesire.malime.account.UserRepository
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.database.dao.SeriesDao
import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.LibraryApi
import timber.log.Timber

/**
 * Repository to interact with a users list of series.
 */
class SeriesRepository(
    private val seriesDao: SeriesDao,
    private val libraryApi: LibraryApi,
    private val userRepository: UserRepository
) {
    /**
     * Observable list of all the users series (Anime + Manga).
     */
    val series: LiveData<List<SeriesModel>> = seriesDao.observe()

    /**
     * Adds the anime series with id [seriesId] to the users tracked list.
     */
    suspend fun addAnime(seriesId: Int, startingStatus: UserSeriesStatus): Resource<SeriesModel> {
        val response = libraryApi.addAnime(retrieveUserId(), seriesId, startingStatus)
        when (response) {
            is Resource.Success -> seriesDao.insert(response.data)
            is Resource.Error -> Timber.e("Error adding anime [$seriesId], ${response.msg}")
        }

        return response
    }

    /**
     * Adds the manga series with id [seriesId] to the users tracked list.
     */
    suspend fun addManga(seriesId: Int, startingStatus: UserSeriesStatus): Resource<SeriesModel> {
        val response = libraryApi.addManga(retrieveUserId(), seriesId, startingStatus)
        when (response) {
            is Resource.Success -> seriesDao.insert(response.data)
            is Resource.Error -> Timber.e("Error adding manga [$seriesId], ${response.msg}")
        }

        return response
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
        val response = libraryApi.retrieveAnime(retrieveUserId())
        when (response) {
            is Resource.Success -> seriesDao.insert(response.data)
            is Resource.Error -> Timber.e("Error refreshing anime, ${response.msg}")
        }

        return response
    }

    /**
     * Pulls and stores all of the users manga list.
     */
    suspend fun refreshManga(): Resource<List<SeriesModel>> {
        val response = libraryApi.retrieveManga(retrieveUserId())
        when (response) {
            is Resource.Success -> seriesDao.insert(response.data)
            is Resource.Error -> Timber.e("Error refreshing manga, ${response.msg}")
        }

        return response
    }

    private suspend fun retrieveUserId() = requireNotNull(userRepository.retrieveUserId())

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
        when (response) {
            is Resource.Success -> seriesDao.update(response.data)
            is Resource.Error -> Timber.e("Error updating series [$userSeriesId], ${response.msg}")
        }

        return response
    }
}
