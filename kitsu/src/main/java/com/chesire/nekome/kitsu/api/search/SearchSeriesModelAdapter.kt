package com.chesire.nekome.kitsu.api.search

import com.chesire.nekome.kitsu.extensions.convertToSeriesModels
import com.squareup.moshi.FromJson

class SearchSeriesModelAdapter {
    @FromJson
    fun modelsFromResponse(response: SearchResponse) = response.data.convertToSeriesModels()
}
