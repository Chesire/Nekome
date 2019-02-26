package com.chesire.malime.core.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chesire.malime.core.flags.SeriesStatus
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.flags.Subtype
import com.chesire.malime.core.flags.UserSeriesStatus

@Entity
data class SeriesModel(
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
    val coverImage: ImageModel,
    val nsfw: Boolean,
    val startDate: String,
    val endDate: String
)
