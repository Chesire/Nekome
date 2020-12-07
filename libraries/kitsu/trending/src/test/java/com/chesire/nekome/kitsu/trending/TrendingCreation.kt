package com.chesire.nekome.kitsu.trending

import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.trending.dto.TrendingItemDto

/**
 * Create a [TrendingItemDto] for tests.
 */
fun createTrendingItemDto(type: SeriesType) =
    TrendingItemDto(
        0,
        type,
        TrendingItemDto.Attributes(
            "canonicalTitle",
            ImageModel.empty
        )
    )
