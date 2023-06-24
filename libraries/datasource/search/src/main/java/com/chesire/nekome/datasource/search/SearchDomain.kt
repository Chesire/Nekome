package com.chesire.nekome.datasource.search

import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.models.ImageModel

/**
 * Domain related to a singular searched item.
 */
data class SearchDomain(
    val id: Int,
    val type: SeriesType,
    val synopsis: String,
    val canonicalTitle: String,
    val otherTitles: Map<String, String>,
    val subtype: Subtype,
    val posterImage: ImageModel
)
