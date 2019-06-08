package com.chesire.malime.flow.series.list.anime

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.extensions.visibleIf
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_item_series.adapterItemSeriesImage
import kotlinx.android.synthetic.main.adapter_item_series.adapterItemSeriesPlusOne
import kotlinx.android.synthetic.main.adapter_item_series.adapterItemSeriesProgress
import kotlinx.android.synthetic.main.adapter_item_series.adapterItemSeriesSubtype
import kotlinx.android.synthetic.main.adapter_item_series.adapterItemSeriesTitle

class SeriesViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
    private lateinit var seriesModel: SeriesModel
    override val containerView: View?
        get() = itemView

    fun bind(model: SeriesModel) {
        seriesModel = model

        Glide.with(itemView)
            .load(model.posterImage.smallest?.url)
            .into(adapterItemSeriesImage)
        adapterItemSeriesTitle.text = model.title
        adapterItemSeriesSubtype.text = model.subtype.name
        adapterItemSeriesProgress.text = "${model.progress} / ${model.totalLength}"
        adapterItemSeriesPlusOne.visibleIf(invisible = true) { model.progress < model.totalLength }
        ViewCompat.setTransitionName(adapterItemSeriesImage, model.title)
    }

    fun bindListener(listener: SeriesInteractionListener) {
        itemView.setOnClickListener { listener.seriesSelected(adapterItemSeriesImage, seriesModel) }
        adapterItemSeriesPlusOne.setOnClickListener { listener.onPlusOne(seriesModel) }
    }
}
