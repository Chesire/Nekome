package com.chesire.nekome.datasource.trending

import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.models.ImageModel

/**
 * Domain class for a trending item.
 */
data class TrendingDomain(
    val id: Int,
    val type: SeriesType,
    val canonicalTitle: String,
    val posterImage: ImageModel
)
