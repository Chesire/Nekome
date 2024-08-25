package com.chesire.nekome.datasource.series

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel

/**
 * Domain object for a single Series item.
 */
data class SeriesDomain(
    val id: Int,
    val userId: Int,
    val type: SeriesType,
    val subtype: Subtype,
    val slug: String,
    val title: String,
    val otherTitles: Map<String, String>,
    val seriesStatus: SeriesStatus,
    val userSeriesStatus: UserSeriesStatus,
    val progress: Int,
    val totalLength: Int,
    val volumesOwned: Int?,
    val volumeCount: Int?,
    val rating: Int,
    val posterImage: ImageModel,
    val startDate: String,
    val endDate: String
)
