package com.chesire.nekome.app.discover.trending

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.chesire.nekome.app.discover.R
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.server.api.TrendingApi

/**
 * Adapter to aid with displaying the trending items pulled from [TrendingApi].
 */
class TrendingAdapter : ListAdapter<SeriesModel, TrendingViewHolder>(SeriesModel.DiffCallback()) {
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
