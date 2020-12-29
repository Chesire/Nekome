package com.chesire.nekome.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.seriesflags.SeriesStatus
import com.chesire.nekome.seriesflags.SeriesType
import com.chesire.nekome.seriesflags.Subtype
import com.chesire.nekome.seriesflags.UserSeriesStatus

/**
 * Data for a singular series entity.
 */
@Entity
data class SeriesEntity(
    @PrimaryKey
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
