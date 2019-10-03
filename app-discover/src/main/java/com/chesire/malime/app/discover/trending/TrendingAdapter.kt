package com.chesire.malime.app.discover.trending

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.chesire.malime.app.discover.R
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.server.api.TrendingApi

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
