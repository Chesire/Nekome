package com.chesire.nekome.kitsu.trending

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.datasource.trending.TrendingDomain
import com.chesire.nekome.kitsu.trending.dto.TrendingItemDto
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
            input.attributes.canonicalTitle,
            input.attributes.posterImage ?: ImageModel.empty
        )
}
