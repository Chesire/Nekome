package com.chesire.malime.kitsu.api.library

import com.chesire.malime.core.models.ImageModel
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.kitsu.api.search.SearchResponse
import com.squareup.moshi.FromJson

@Suppress("unused")
class LibrarySeriesModelAdapter {
    @FromJson
    fun seriesModelsFromLibraryResponse(response: LibraryResponse): List<SeriesModel> {
        return emptyList()
    }
}
