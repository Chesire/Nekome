package com.chesire.nekome.kitsu.trending

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
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
            "slug",
            "synopsis",
            "canonicalTitle",
            "startDate",
            "endDate",
            Subtype.Unknown,
            SeriesStatus.Unknown,
            ImageModel.empty,
            ImageModel.empty,
            0,
            0,
            false
        )
    )
