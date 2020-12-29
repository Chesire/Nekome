package com.chesire.nekome.kitsu.search

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.search.dto.SearchItemDto
import com.chesire.nekome.seriesflags.SeriesType
import com.chesire.nekome.seriesflags.Subtype

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
