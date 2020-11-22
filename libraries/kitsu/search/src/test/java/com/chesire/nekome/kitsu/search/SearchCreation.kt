package com.chesire.nekome.kitsu.search

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.models.ImageModel

/**
 * Create a [KitsuSearchEntity] for tests.
 */
fun createKitsuSearchingEntity(type: SeriesType) = KitsuSearchEntity(
    0,
    type,
    KitsuSearchEntity.EntityAttributes(
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
