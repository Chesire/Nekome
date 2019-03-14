package com.chesire.malime.repo

import androidx.lifecycle.LiveData
import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.flags.SeriesType
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

    suspend fun refreshAnime() {
        val response = libraryApi.retrieveAnime(retrieveUserId())
        when (response) {
            is Resource.Success -> seriesDao.insert(response.data)
            is Resource.Error -> Timber.e("Error refreshing anime")
        }
    }

    suspend fun refreshManga() {
        val response = libraryApi.retrieveManga(retrieveUserId())
        when (response) {
            is Resource.Success -> seriesDao.insert(response.data)
            is Resource.Error -> Timber.e("Error refreshing manga")
        }
    }

    // Force (!!) the userId, it should not be null at this point
    private suspend fun retrieveUserId() = userRepository.retrieveUserId()!!
}
