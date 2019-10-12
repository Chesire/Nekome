package com.chesire.malime.kitsu.api.trending

import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.kitsu.extensions.convertToSeriesModels
import com.squareup.moshi.FromJson

/**
 * Adapter to convert classes of [TrendingResponse] into a [List] of [SeriesModel].
 */
class TrendingAdapter {
    @FromJson
    fun modelsFromResponse(response: TrendingResponse) = response.data.convertToSeriesModels()
}
