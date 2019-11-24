package com.chesire.nekome.harness

import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.TrendingApi

open class FakeTrendingApi : TrendingApi {
    override suspend fun trendingAnime(): Resource<List<SeriesModel>> {
        TODO("not implemented")
    }

    override suspend fun trendingManga(): Resource<List<SeriesModel>> {
        TODO("not implemented")
    }
}
