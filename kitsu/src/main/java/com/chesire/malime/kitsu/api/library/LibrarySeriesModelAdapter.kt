package com.chesire.malime.kitsu.api.library

import com.chesire.malime.kitsu.createSeriesModel
import com.squareup.moshi.FromJson

class LibrarySeriesModelAdapter {
    @FromJson
    fun seriesModelFromAddResponse(response: AddResponse) =
        createSeriesModel(response.data, response.series.first())
}
