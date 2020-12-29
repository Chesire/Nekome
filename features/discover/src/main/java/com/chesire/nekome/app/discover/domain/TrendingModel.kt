package com.chesire.nekome.app.discover.domain

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.seriesflags.SeriesType
import kotlinx.parcelize.Parcelize

/**
 * Model containing information to show for a trending item.
 */
@Parcelize
data class TrendingModel(
    val id: Int,
    val type: SeriesType,
    val canonicalTitle: String,
    val posterImage: ImageModel
) : Parcelable

/**
 * Provides a [DiffUtil.ItemCallback] class for use with the [TrendingModel].
 */
class DiffCallback : DiffUtil.ItemCallback<TrendingModel>() {
    override fun areItemsTheSame(oldItem: TrendingModel, newItem: TrendingModel) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TrendingModel, newItem: TrendingModel) =
        oldItem == newItem
}
