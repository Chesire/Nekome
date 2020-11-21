package com.chesire.nekome.library

import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.library.api.LibraryApi
import com.chesire.nekome.library.api.LibraryEntity
import timber.log.Timber

/**
 * Repository to interact with a users list of series.
 */
class SeriesRepository(
    private val seriesDao: SeriesDao,
    private val libraryApi: LibraryApi,
    private val userProvider: UserProvider
) {

    /**
     * Observable list of all the users series (Anime + Manga).
     */
    fun getSeries() = seriesDao.getSeries()

    /**
     * Adds the anime series with id [seriesId] to the users tracked list.
     */
    suspend fun addAnime(seriesId: Int, startingStatus: UserSeriesStatus): Resource<SeriesModel> {
        return when (
            val response =
                libraryApi.addAnime(
                    userProvider.provideUserId(),
                    seriesId,
                    startingStatus
                )
            ) {
            is Resource.Success -> {
                val newData = mapToSeriesModel(response.data)
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
        return when (
            val response =
                libraryApi.addManga(
                    userProvider.provideUserId(),
                    seriesId,
                    startingStatus
                )
            ) {
            is Resource.Success -> {
                val newData = mapToSeriesModel(response.data)
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
        return when (
            val response = libraryApi.retrieveAnime(userProvider.provideUserId())) {
            is Resource.Success -> {
                val add = response.data.map { mapToSeriesModel(it) }
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
        return when (
            val response = libraryApi.retrieveManga(userProvider.provideUserId())) {
            is Resource.Success -> {
                val add = response.data.map { mapToSeriesModel(it) }
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
        return when (
            val response = libraryApi.update(userSeriesId, progress, status)) {
            is Resource.Success -> {
                val add = mapToSeriesModel(response.data)
                seriesDao.update(add)
                response.with(add)
            }
            is Resource.Error -> {
                Timber.e("Error updating series [$userSeriesId], ${response.msg}")
                response.morph()
            }
        }
    }

    private fun mapToSeriesModel(libraryEntity: LibraryEntity): SeriesModel {
        return SeriesModel(
            libraryEntity.id,
            libraryEntity.userId,
            libraryEntity.type,
            libraryEntity.subtype,
            libraryEntity.slug,
            libraryEntity.synopsis,
            libraryEntity.title,
            libraryEntity.seriesStatus,
            libraryEntity.userSeriesStatus,
            libraryEntity.progress,
            libraryEntity.totalLength,
            libraryEntity.posterImage,
            libraryEntity.coverImage,
            libraryEntity.nsfw,
            libraryEntity.startDate,
            libraryEntity.endDate
        )
    }
}
