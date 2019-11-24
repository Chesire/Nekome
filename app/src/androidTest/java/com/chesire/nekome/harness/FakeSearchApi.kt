package com.chesire.nekome.harness

import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.SearchApi
import com.chesire.nekome.core.models.SeriesModel

open class FakeSearchApi : SearchApi {
    override suspend fun searchForAnime(title: String): Resource<List<SeriesModel>> {
        TODO("not implemented")
    }

    override suspend fun searchForManga(title: String): Resource<List<SeriesModel>> {
        TODO("not implemented")
    }
}
