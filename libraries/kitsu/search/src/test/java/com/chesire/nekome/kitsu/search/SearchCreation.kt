package com.chesire.nekome.kitsu.search

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.dataflags.SeriesType
import com.chesire.nekome.dataflags.Subtype
import com.chesire.nekome.kitsu.search.dto.SearchItemDto

/**
 * Create a [SearchItemDto] for tests.
 */
fun createSearchItemDto(type: SeriesType) =
    SearchItemDto(
        0,
        type,
        SearchItemDto.Attributes(
            "synopsis",
            "canonicalTitle",
            Subtype.Unknown,
            ImageModel.empty
        )
    )
