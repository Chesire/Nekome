package com.chesire.nekome.kitsu.search

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.datasource.search.SearchDomain
import com.chesire.nekome.kitsu.search.dto.SearchItemDto
import javax.inject.Inject

/**
 * Provides ability to map instances of [SearchItemDto] into [SearchDomain].
 */
class SearchItemDtoMapper @Inject constructor() {

    /**
     * Converts an instance of [SearchItemDto] into a [SearchDomain].
     */
    fun toSearchDomain(input: SearchItemDto) =
        SearchDomain(
            input.id,
            input.type,
            input.attributes.synopsis,
            input.attributes.canonicalTitle,
            input.attributes.subtype,
            input.attributes.posterImage ?: ImageModel.empty
        )
}
