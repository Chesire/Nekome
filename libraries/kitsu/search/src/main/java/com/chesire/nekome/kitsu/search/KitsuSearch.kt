package com.chesire.nekome.kitsu.search

import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.search.api.SearchApi

class KitsuSearch : SearchApi {
    override suspend fun searchForAnime(title: String): Resource<List<SeriesModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun searchForManga(title: String): Resource<List<SeriesModel>> {
        TODO("Not yet implemented")
    }
}
