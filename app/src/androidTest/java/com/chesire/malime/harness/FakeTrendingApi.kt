package com.chesire.malime.harness

import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.TrendingApi

open class FakeTrendingApi : TrendingApi {
    override suspend fun trendingAnime(): Resource<List<SeriesModel>> {
        TODO("not implemented")
    }

    override suspend fun trendingManga(): Resource<List<SeriesModel>> {
        TODO("not implemented")
    }
}
