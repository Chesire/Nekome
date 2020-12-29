package com.chesire.nekome.search.api

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.seriesflags.SeriesType
import com.chesire.nekome.seriesflags.Subtype

/**
 * Domain related to a singular searched item.
 */
data class SearchDomain(
    val id: Int,
    val type: SeriesType,
    val synopsis: String,
    val canonicalTitle: String,
    val subtype: Subtype,
    val posterImage: ImageModel
)
