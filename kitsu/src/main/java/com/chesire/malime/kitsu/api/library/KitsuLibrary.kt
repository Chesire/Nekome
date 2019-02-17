package com.chesire.malime.kitsu.api.library

import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.models.SeriesModel

class KitsuLibrary(
    private val libraryService: KitsuLibraryService,
    private val userId: Int
) : LibraryApi {
    override suspend fun retrieveAnime(): List<SeriesModel> {
        return emptyList()
    }

    override suspend fun retrieveManga(): List<SeriesModel> {
        return emptyList()
    }
}
