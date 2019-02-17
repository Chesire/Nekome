package com.chesire.malime.core.api

import com.chesire.malime.core.models.SeriesModel

interface LibraryApi {
    suspend fun retrieveAnime(): List<SeriesModel>
    suspend fun retrieveManga(): List<SeriesModel>
}
