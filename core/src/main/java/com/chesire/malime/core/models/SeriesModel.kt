package com.chesire.malime.core.models

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.chesire.malime.core.flags.SeriesStatus
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.flags.Subtype
import com.chesire.malime.core.flags.UserSeriesStatus
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

    /**
     * Provides a [DiffUtil.ItemCallback] class for use with the [SeriesModel].
     */
    class DiffCallback : DiffUtil.ItemCallback<SeriesModel>() {
        override fun areItemsTheSame(oldItem: SeriesModel, newItem: SeriesModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SeriesModel, newItem: SeriesModel) =
            oldItem == newItem
    }
}
