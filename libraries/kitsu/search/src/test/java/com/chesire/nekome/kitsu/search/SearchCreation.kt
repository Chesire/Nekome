package com.chesire.nekome.kitsu.search

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.search.dto.SearchItemDto

/**
 * Create a [SearchItemDto] for tests.
 */
fun createKitsuSearchingDto(type: SeriesType) =
    SearchItemDto(
        0,
        type,
        SearchItemDto.Attributes(
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
