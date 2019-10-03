package com.chesire.malime.app.discover.trending

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chesire.malime.core.models.SeriesModel
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

        Glide.with(itemView)
            .load(model.posterImage.smallest?.url)
            .into(itemTrendingImage)
        itemTrendingTitle.text = seriesModel.title
    }
}
