package com.chesire.malime.repo

import androidx.lifecycle.LiveData
import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.db.SeriesDao
import timber.log.Timber
import javax.inject.Inject

class SeriesRepository @Inject constructor(
    private val seriesDao: SeriesDao,
    private val libraryApi: LibraryApi,
    private val userRepository: UserRepository
) {
    val anime: LiveData<List<SeriesModel>>
        get() = seriesDao.observe(SeriesType.Anime)

    val manga: LiveData<List<SeriesModel>>
        get() = seriesDao.observe(SeriesType.Manga)

    val series: LiveData<List<SeriesModel>>
        get() = seriesDao.observe()

    suspend fun addAnime(seriesId: Int, startingStatus: UserSeriesStatus): Resource<SeriesModel> {
        val response = libraryApi.addAnime(retrieveUserId(), seriesId, startingStatus)
        when (response) {
            is Resource.Success -> seriesDao.insert(response.data)
            is Resource.Error -> Timber.e("Error adding anime [$seriesId], ${response.msg}")
        }

        return response
    }

    suspend fun addManga(seriesId: Int, startingStatus: UserSeriesStatus): Resource<SeriesModel> {
        val response = libraryApi.addManga(retrieveUserId(), seriesId, startingStatus)
        when (response) {
            is Resource.Success -> seriesDao.insert(response.data)
            is Resource.Error -> Timber.e("Error adding manga [$seriesId], ${response.msg}")
        }

        return response
    }

    suspend fun refreshAnime(): Resource<List<SeriesModel>> {
        val response = libraryApi.retrieveAnime(retrieveUserId())
        when (response) {
            is Resource.Success -> seriesDao.insert(response.data)
            is Resource.Error -> Timber.e("Error refreshing anime, ${response.msg}")
        }

        return response
    }

    suspend fun refreshManga(): Resource<List<SeriesModel>> {
        val response = libraryApi.retrieveManga(retrieveUserId())
        when (response) {
            is Resource.Success -> seriesDao.insert(response.data)
            is Resource.Error -> Timber.e("Error refreshing manga, ${response.msg}")
        }

        return response
    }

    private suspend fun retrieveUserId() = requireNotNull(userRepository.retrieveUserId())

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
