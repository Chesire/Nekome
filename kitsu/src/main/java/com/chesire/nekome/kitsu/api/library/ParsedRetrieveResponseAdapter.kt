package com.chesire.nekome.kitsu.api.library

import com.chesire.nekome.kitsu.createSeriesModel
import com.squareup.moshi.FromJson

class ParsedRetrieveResponseAdapter {
    @FromJson
    fun parseRetrieveResponse(response: RetrieveResponse): ParsedRetrieveResponse {
        val seriesModels = response
            .data
            .mapNotNull { library ->
                val seriesId = library.relationships.anime?.data?.id
                    ?: library.relationships.manga?.data?.id

                response.series
                    .find { it.id == seriesId }
                    ?.let { series ->
                        return@mapNotNull createSeriesModel(library, series)
                    }
            }

        return ParsedRetrieveResponse(seriesModels, response.links)
    }
}
