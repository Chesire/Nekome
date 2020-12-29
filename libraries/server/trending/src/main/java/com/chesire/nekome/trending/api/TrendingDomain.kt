package com.chesire.nekome.trending.api

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.dataflags.SeriesType

/**
 * Domain class for a trending item.
 */
data class TrendingDomain(
    val id: Int,
    val type: SeriesType,
    val canonicalTitle: String,
    val posterImage: ImageModel
)
