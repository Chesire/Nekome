package com.chesire.malime.kitsu.api.search

import com.chesire.malime.kitsu.extensions.convertToSeriesModels
import com.squareup.moshi.FromJson

class SearchSeriesModelAdapter {
    @FromJson
    fun modelsFromResponse(response: SearchResponse) = response.data.convertToSeriesModels()
}
