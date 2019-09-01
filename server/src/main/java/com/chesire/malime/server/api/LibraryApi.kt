package com.chesire.malime.server.api

import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.server.Resource

interface LibraryApi {
    suspend fun retrieveAnime(userId: Int): Resource<List<SeriesModel>>
    suspend fun retrieveManga(userId: Int): Resource<List<SeriesModel>>
    suspend fun addAnime(
        userId: Int,
        seriesId: Int,
        startingStatus: UserSeriesStatus
    ): Resource<SeriesModel>

    suspend fun addManga(
        userId: Int,
        seriesId: Int,
        startingStatus: UserSeriesStatus
    ): Resource<SeriesModel>

    suspend fun update(
        userSeriesId: Int,
        progress: Int,
        newStatus: UserSeriesStatus
    ): Resource<SeriesModel>

    suspend fun delete(userSeriesId: Int): Resource<Any>
}
