package com.chesire.nekome.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.core.models.LinkModel

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
    val rating: Int,
    val posterImage: ImageModel,
    val startDate: String,
    val endDate: String,
    val links: List<LinkModel>
)
