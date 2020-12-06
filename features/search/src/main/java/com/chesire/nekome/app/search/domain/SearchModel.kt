package com.chesire.nekome.app.search.domain

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.models.ImageModel
import kotlinx.android.parcel.Parcelize

/**
 * Model containing information to show for a searched item.
 */
@Parcelize
data class SearchModel(
    val id: Int,
    val type: SeriesType,
    val synopsis: String,
    val canonicalTitle: String,
    val subtype: Subtype,
    val posterImage: ImageModel
) : Parcelable

/**
 * Provides a [DiffUtil.ItemCallback] class for use with the [SearchModel].
 */
class DiffCallback : DiffUtil.ItemCallback<SearchModel>() {
    override fun areItemsTheSame(oldItem: SearchModel, newItem: SearchModel) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SearchModel, newItem: SearchModel) =
        oldItem == newItem
}
