package com.chesire.nekome.kitsu.api.trending

import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.kitsu.extensions.convertToSeriesModels
import com.squareup.moshi.FromJson

/**
 * Adapter to convert classes of [TrendingResponse] into a [List] of [SeriesModel].
 */
class TrendingAdapter {
    /**
     * Converts [response] into a [List] of [SeriesModel].
     */
    @FromJson
    fun modelsFromResponse(response: TrendingResponse) = response.data.convertToSeriesModels()
}
