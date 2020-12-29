package com.chesire.nekome.kitsu.trending

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.trending.dto.TrendingItemDto
import com.chesire.nekome.seriesflags.SeriesType

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
