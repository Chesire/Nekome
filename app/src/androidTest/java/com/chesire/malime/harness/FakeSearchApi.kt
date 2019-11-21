package com.chesire.malime.harness

import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.SearchApi
import com.chesire.malime.core.models.SeriesModel

open class FakeSearchApi : SearchApi {
    override suspend fun searchForAnime(title: String): Resource<List<SeriesModel>> {
        TODO("not implemented")
    }

    override suspend fun searchForManga(title: String): Resource<List<SeriesModel>> {
        TODO("not implemented")
    }
}
