package com.chesire.malime.core.api

import com.chesire.malime.core.models.SeriesModel

interface LibraryApi {
    suspend fun retrieveLibrary(): List<SeriesModel>
}
