package com.chesire.nekome.search.api

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.models.ImageModel

/**
 * Domain related to a singular searched item.
 */
data class SearchDomain(
    val id: Int,
    val type: SeriesType,
    val slug: String,
    val synopsis: String,
    val canonicalTitle: String,
    val startDate: String?,
    val endDate: String?,
    val subtype: Subtype,
    val status: SeriesStatus,
    val posterImage: ImageModel?,
    val coverImage: ImageModel?,
    val chapterCount: Int?,
    val episodeCount: Int?,
    val nsfw: Boolean = false
)
