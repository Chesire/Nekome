package com.chesire.nekome.app.series.list.view

import androidx.recyclerview.widget.DiffUtil
import com.chesire.nekome.datasource.series.SeriesDomain

/**
 * Provides a [DiffUtil.ItemCallback] class for use with the [SeriesDomain].
 */
class SeriesDomainDiffCallback : DiffUtil.ItemCallback<SeriesDomain>() {
    override fun areItemsTheSame(oldItem: SeriesDomain, newItem: SeriesDomain) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SeriesDomain, newItem: SeriesDomain) =
        oldItem == newItem
}
