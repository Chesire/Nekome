package com.chesire.nekome.app.discover.trending

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.chesire.nekome.app.discover.databinding.AdapterItemTrendingBinding
import com.chesire.nekome.app.discover.domain.TrendingModel

/**
 * ViewHolder for the Trending items in discover.
 */
class TrendingViewHolder(
    private val binding: AdapterItemTrendingBinding
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var trendingModel: TrendingModel

    /**
     * Binds the [model] data to the view.
     */
    fun bind(model: TrendingModel) {
        trendingModel = model

        binding.itemTrendingImage.load(model.posterImage.smallest?.url)
        binding.itemTrendingTitle.text = trendingModel.canonicalTitle
    }
}
