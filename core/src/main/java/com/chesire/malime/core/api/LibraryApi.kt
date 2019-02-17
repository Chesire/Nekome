package com.chesire.malime.core.api

import com.chesire.malime.core.Resource
import com.chesire.malime.core.models.SeriesModel

interface LibraryApi {
    suspend fun retrieveAnime(): Resource<List<SeriesModel>>
    suspend fun retrieveManga(): Resource<List<SeriesModel>>
}
