package com.chesire.malime.harness

import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.LibraryApi
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel

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
