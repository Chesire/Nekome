package com.chesire.malime.server.api

import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.server.Resource

interface SearchApi {
    suspend fun searchForAnime(title: String): Resource<List<SeriesModel>>
    suspend fun searchForManga(title: String): Resource<List<SeriesModel>>
}
