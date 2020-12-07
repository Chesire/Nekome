package com.chesire.nekome.app.discover.trending

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.chesire.nekome.app.discover.R
import com.chesire.nekome.app.discover.domain.DiffCallback
import com.chesire.nekome.app.discover.domain.TrendingModel

/**
 * Adapter to aid with displaying the trending results.
 */
class TrendingAdapter : ListAdapter<TrendingModel, TrendingViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        return TrendingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_item_trending,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
