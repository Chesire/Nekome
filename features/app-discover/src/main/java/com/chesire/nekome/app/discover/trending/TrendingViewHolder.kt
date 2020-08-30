package com.chesire.nekome.app.discover.trending

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.chesire.nekome.core.models.SeriesModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_item_trending.itemTrendingImage
import kotlinx.android.synthetic.main.adapter_item_trending.itemTrendingTitle

/**
 * ViewHolder for the Trending items in discover.
 */
class TrendingViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
    private lateinit var seriesModel: SeriesModel
    override val containerView: View?
        get() = itemView

    /**
     * Binds the [model] data to the view.
     */
    fun bind(model: SeriesModel) {
        seriesModel = model

        itemTrendingImage.load(model.posterImage.smallest?.url)
        itemTrendingTitle.text = seriesModel.title
    }
}
