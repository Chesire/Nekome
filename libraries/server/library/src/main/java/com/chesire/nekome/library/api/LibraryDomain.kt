package com.chesire.nekome.library.api

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.dataflags.SeriesStatus
import com.chesire.nekome.dataflags.SeriesType
import com.chesire.nekome.dataflags.Subtype
import com.chesire.nekome.dataflags.UserSeriesStatus

/**
 * Domain related to a singular library item.
 */
data class LibraryDomain(
    val id: Int,
    val userId: Int,
    val type: SeriesType,
    val subtype: Subtype,
    val slug: String,
    val title: String,
    val seriesStatus: SeriesStatus,
    val userSeriesStatus: UserSeriesStatus,
    val progress: Int,
    val totalLength: Int,
    val posterImage: ImageModel,
    val startDate: String,
    val endDate: String
)
