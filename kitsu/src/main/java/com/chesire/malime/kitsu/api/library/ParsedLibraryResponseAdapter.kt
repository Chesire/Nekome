package com.chesire.malime.kitsu.api.library

import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.kitsu.api.intermediaries.Links
import com.squareup.moshi.FromJson

@Suppress("unused")
class ParsedLibraryResponseAdapter {
    @FromJson
    fun parsedLibraryResponseFromLibraryResponse(response: LibraryResponse): ParsedLibraryResponse {
        return ParsedLibraryResponse(emptyList(), Links("", "", ""))
    }
    //@FromJson
    //fun seriesModelsFromLibraryResponse(response: LibraryResponse): Pair<List<SeriesModel>, Links> {
    //    return emptyList<SeriesModel>() to response.links
    //}
}
