package com.chesire.nekome.kitsu.api.library

import com.chesire.nekome.kitsu.createSeriesModel
import com.squareup.moshi.FromJson

class LibrarySeriesModelAdapter {
    @FromJson
    fun seriesModelFromAddResponse(response: AddResponse) =
        createSeriesModel(response.data, response.series.first())
}
