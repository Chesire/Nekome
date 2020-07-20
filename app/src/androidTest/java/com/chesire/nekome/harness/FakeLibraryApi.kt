package com.chesire.nekome.harness

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.LibraryApi

open class FakeLibraryApi : LibraryApi {
    override suspend fun retrieveAnime(userId: Int): Resource<List<SeriesModel>> {
        TODO("not implemented")
    }

    override suspend fun retrieveManga(userId: Int): Resource<List<SeriesModel>> {
        TODO("not implemented")
    }

    override suspend fun addAnime(
        userId: Int,
        seriesId: Int,
        startingStatus: UserSeriesStatus
    ): Resource<SeriesModel> {
        TODO("not implemented")
    }

    override suspend fun addManga(
        userId: Int,
        seriesId: Int,
        startingStatus: UserSeriesStatus
    ): Resource<SeriesModel> {
        TODO("not implemented")
    }

    override suspend fun update(
        userSeriesId: Int,
        progress: Int,
        newStatus: UserSeriesStatus
    ): Resource<SeriesModel> {
        TODO("not implemented")
    }

    override suspend fun delete(userSeriesId: Int): Resource<Any> {
        TODO("not implemented")
    }
}
