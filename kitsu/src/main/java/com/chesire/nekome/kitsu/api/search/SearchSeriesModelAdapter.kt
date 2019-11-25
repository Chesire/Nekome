package com.chesire.nekome.kitsu.api.search

import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.kitsu.extensions.convertToSeriesModels
import com.squareup.moshi.FromJson

/**
 * Adapter to convert a [SearchResponse] into a [List] of [SeriesModel].
 */
class SearchSeriesModelAdapter {
    /**
     * Converts [response] into a [List] of [SeriesModel].
     */
    @FromJson
    fun modelsFromResponse(response: SearchResponse) = response.data.convertToSeriesModels()
}
