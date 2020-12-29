package com.chesire.nekome.library

import android.os.Parcelable
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.seriesflags.SeriesStatus
import com.chesire.nekome.seriesflags.SeriesType
import com.chesire.nekome.seriesflags.Subtype
import com.chesire.nekome.seriesflags.UserSeriesStatus
import kotlinx.parcelize.Parcelize

/**
 * Domain object for a single Series item.
 */
@Parcelize
data class SeriesDomain(
    val id: Int,
    val userId: Int,
    val type: SeriesType,
    val subtype: Subtype,
    val slug: String,
    val canonicalTitle: String,
    val seriesStatus: SeriesStatus,
    val userSeriesStatus: UserSeriesStatus,
    val progress: Int,
    val totalLength: Int,
    val posterImage: ImageModel,
    val startDate: String,
    val endDate: String
) : Parcelable
