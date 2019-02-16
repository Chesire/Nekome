package com.chesire.malime.core.models

import com.chesire.malime.core.flags.SeriesStatus
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.flags.Subtype

data class SeriesModel(
    val id: Int,
    val type: SeriesType,
    val subtype: Subtype,
    val slug: String,
    val title: String,
    val seriesStatus: SeriesStatus,
    val progress: Int,
    val totalLength: Int,
    val posterImage: ImageModel,
    val coverImage: ImageModel,
    val nsfw: Boolean,
    val startDate: String,
    val endDate: String
)
