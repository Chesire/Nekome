package com.chesire.malime.core.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chesire.malime.server.flags.SeriesStatus
import com.chesire.malime.server.flags.SeriesType
import com.chesire.malime.server.flags.Subtype
import com.chesire.malime.server.flags.UserSeriesStatus
import kotlinx.android.parcel.Parcelize

@Parcelize
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
) : Parcelable
