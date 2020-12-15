package com.chesire.nekome.core.models

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

/**
 * Data for a singular series entity.
 */
@Parcelize
@Entity
@JsonClass(generateAdapter = true)
data class SeriesModel(
    @PrimaryKey
    val id: Int,
    val userId: Int,
    val type: SeriesType,
    val subtype: Subtype,
    val slug: String,
    val synopsis: String,
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
) : Parcelable {
    @Ignore
    @IgnoredOnParcel
    val lengthKnown = totalLength != 0
}
