package com.chesire.nekome.app.search.domain

import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype

/**
 * Model containing information to show for a searched item.
 */
data class SearchModel(
    val id: Int,
    val type: SeriesType,
    val synopsis: String,
    val canonicalTitle: String,
    val subtype: Subtype
)
