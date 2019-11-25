package com.chesire.nekome.kitsu.api.library

import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.kitsu.createSeriesModel
import com.squareup.moshi.FromJson

/**
 * Adapter for converting an [AddResponse] to a [SeriesModel].
 */
class LibrarySeriesModelAdapter {
    /**
     * Converts [response] into a [SeriesModel] object.
     */
    @FromJson
    fun seriesModelFromAddResponse(response: AddResponse) =
        createSeriesModel(response.data, response.series.first())
}
