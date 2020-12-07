package com.chesire.nekome.app.discover.trending

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.chesire.nekome.app.discover.domain.TrendingModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_item_trending.itemTrendingImage
import kotlinx.android.synthetic.main.adapter_item_trending.itemTrendingTitle

/**
 * ViewHolder for the Trending items in discover.
 */
class TrendingViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
    private lateinit var trendingModel: TrendingModel
    override val containerView: View
        get() = itemView

    /**
     * Binds the [model] data to the view.
     */
    fun bind(model: TrendingModel) {
        trendingModel = model

        itemTrendingImage.load(model.posterImage.smallest?.url)
        itemTrendingTitle.text = trendingModel.canonicalTitle
    }
}
