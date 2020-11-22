package com.chesire.nekome.kitsu.trending

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.models.ImageModel

/**
 * Create a [KitsuTrendingEntity] for tests.
 */
fun createKitsuTrendingEntity(type: SeriesType) = KitsuTrendingEntity(
    0,
    type,
    KitsuTrendingEntity.EntityAttributes(
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
