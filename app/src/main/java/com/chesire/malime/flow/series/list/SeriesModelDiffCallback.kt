package com.chesire.malime.flow.series.list

import androidx.recyclerview.widget.DiffUtil
import com.chesire.malime.server.models.SeriesModel

/**
 * Provides a [DiffUtil.ItemCallback] class for use with the [SeriesModel].
 */
class SeriesModelDiffCallback : DiffUtil.ItemCallback<SeriesModel>() {
    override fun areItemsTheSame(oldItem: SeriesModel, newItem: SeriesModel) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SeriesModel, newItem: SeriesModel) =
        oldItem == newItem
}
