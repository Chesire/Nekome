package com.chesire.nekome.kitsu.trending

import com.chesire.nekome.kitsu.trending.dto.TrendingItemDto
import com.chesire.nekome.trending.api.TrendingDomain
import javax.inject.Inject

/**
 * Provides ability to map instances of [TrendingItemDto] into [TrendingDomain].
 */
class TrendingItemDtoMapper @Inject constructor() {

    /**
     * Converts an instance of [TrendingItemDto] into a [TrendingDomain].
     */
    fun toTrendingDomain(input: TrendingItemDto) =
        TrendingDomain(
            input.id,
            input.type,
            input.attributes.slug,
            input.attributes.synopsis,
            input.attributes.canonicalTitle,
            "",
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
