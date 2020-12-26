package com.chesire.nekome.app.discover.trending

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.chesire.nekome.app.discover.databinding.AdapterItemTrendingBinding
import com.chesire.nekome.app.discover.domain.DiffCallback
import com.chesire.nekome.app.discover.domain.TrendingModel

/**
 * Adapter to aid with displaying the trending results.
 */
class TrendingAdapter : ListAdapter<TrendingModel, TrendingViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        return TrendingViewHolder(
            AdapterItemTrendingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
