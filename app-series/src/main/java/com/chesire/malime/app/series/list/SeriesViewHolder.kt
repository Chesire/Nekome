package com.chesire.malime.app.series.list

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chesire.malime.core.extensions.hide
import com.chesire.malime.core.extensions.show
import com.chesire.malime.core.extensions.toAlpha
import com.chesire.malime.core.extensions.visibleIf
import com.chesire.malime.core.models.SeriesModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_item_series.adapterItemProgressBar
import kotlinx.android.synthetic.main.adapter_item_series.adapterItemSeriesImage
import kotlinx.android.synthetic.main.adapter_item_series.adapterItemSeriesPlusOne
import kotlinx.android.synthetic.main.adapter_item_series.adapterItemSeriesProgress
import kotlinx.android.synthetic.main.adapter_item_series.adapterItemSeriesSubtype
import kotlinx.android.synthetic.main.adapter_item_series.adapterItemSeriesTitle

/**
 * ViewHolder for Series items in the Anime or Manga list.
 */
class SeriesViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
    private lateinit var seriesModel: SeriesModel
    override val containerView: View
        get() = itemView

    /**
     * Binds the [model] data to the view.
     */
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

    /**
     * Binds the [listener] to the view.
     */
    fun bindListener(listener: SeriesInteractionListener) {
        itemView.setOnClickListener { listener.seriesSelected(adapterItemSeriesImage, seriesModel) }
        adapterItemSeriesPlusOne.setOnClickListener {
            startUpdatingSeries()
            listener.onPlusOne(seriesModel) {
                finishUpdatingSeries()
            }
        }
    }

    private fun startUpdatingSeries() {
        adapterItemProgressBar.show()
        adapterItemSeriesPlusOne.isEnabled = false
        adapterItemSeriesPlusOne.toAlpha(0.3f)
    }

    private fun finishUpdatingSeries() {
        adapterItemProgressBar.hide()
        adapterItemSeriesPlusOne.isEnabled = true
        adapterItemSeriesPlusOne.toAlpha(1f)
    }
}
