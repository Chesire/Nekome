package com.chesire.malime.app.discover.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chesire.malime.core.models.SeriesModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_item_search.itemSearchImage
import kotlinx.android.synthetic.main.adapter_item_search.itemSearchPlusOne
import kotlinx.android.synthetic.main.adapter_item_search.itemSearchTitle

class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
    private lateinit var seriesModel: SeriesModel
    override val containerView: View?
        get() = itemView

    fun bind(model: SeriesModel) {
        seriesModel = model
        itemSearchTitle.text = model.title
        Glide.with(itemView)
            .load(model.coverImage.smallest?.url)
            .into(itemSearchImage)
    }

    fun bindListener(listener: SearchInteractionListener) {
        itemSearchPlusOne.setOnClickListener { listener.addSeries(seriesModel) }
    }
}
