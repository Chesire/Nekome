package com.chesire.nekome.app.series.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chesire.nekome.app.series.R
import com.chesire.nekome.core.extensions.hide
import com.chesire.nekome.core.extensions.show
import com.chesire.nekome.core.extensions.toAlpha
import com.chesire.nekome.core.extensions.visibleIf
import com.chesire.nekome.core.models.SeriesModel
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

        Glide.with(containerView)
            .load(model.posterImage.smallest?.url)
            .into(adapterItemSeriesImage)
        adapterItemSeriesTitle.text = model.title
        adapterItemSeriesSubtype.text = model.subtype.name
        adapterItemSeriesProgress.text = containerView.context.getString(
            R.string.series_list_length,
            model.progress.toString(),
            if (model.lengthKnown) model.totalLength else '-'
        )
        adapterItemSeriesPlusOne.visibleIf(invisible = true) {
            !model.lengthKnown || model.progress < model.totalLength
        }
    }

    /**
     * Binds the [listener] to the view.
     */
    fun bindListener(listener: SeriesInteractionListener) {
        containerView.setOnClickListener {
            listener.seriesSelected(
                adapterItemSeriesImage,
                seriesModel
            )
        }
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
