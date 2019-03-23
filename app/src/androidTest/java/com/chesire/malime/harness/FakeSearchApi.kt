package com.chesire.malime.harness

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.models.SeriesModel

class FakeSearchApi : SearchApi {
    override suspend fun searchForAnime(title: String): Resource<List<SeriesModel>> {
        TODO("not implemented")
    }

    override suspend fun searchForManga(title: String): Resource<List<SeriesModel>> {
        TODO("not implemented")
    }
}
