package com.chesire.nekome.kitsu.search

import com.chesire.nekome.kitsu.search.dto.SearchItemDto
import com.chesire.nekome.search.api.SearchDomain
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
            input.attributes.slug,
            input.attributes.synopsis,
            input.attributes.canonicalTitle,
            input.attributes.startDate,
            input.attributes.endDate,
            input.attributes.subtype,
            input.attributes.status,
            input.attributes.posterImage,
            input.attributes.coverImage,
            input.attributes.chapterCount,
            input.attributes.episodeCount,
            input.attributes.nsfw
        )
}
