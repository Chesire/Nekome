package com.chesire.malime.core.api

import com.chesire.malime.core.Resource
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel

interface LibraryApi {
    suspend fun retrieveAnime(): Resource<List<SeriesModel>>
    suspend fun retrieveManga(): Resource<List<SeriesModel>>
    suspend fun addAnime(seriesId: Int, startingStatus: UserSeriesStatus): Resource<SeriesModel>
    suspend fun addManga(seriesId: Int, startingStatus: UserSeriesStatus): Resource<SeriesModel>
    suspend fun delete(userSeriesId: Int): Resource<Any>
}
