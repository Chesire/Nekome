package com.chesire.nekome.app.series.list.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chesire.nekome.app.series.R
import com.chesire.nekome.app.series.list.SeriesInteractionListener
import com.chesire.nekome.core.extensions.hide
import com.chesire.nekome.core.extensions.show
import com.chesire.nekome.core.extensions.toAlpha
import com.chesire.nekome.core.extensions.visibleIf
import com.chesire.nekome.core.models.SeriesModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_item_series.seriesDate
import kotlinx.android.synthetic.main.adapter_item_series.seriesImage
import kotlinx.android.synthetic.main.adapter_item_series.seriesPlusOne
import kotlinx.android.synthetic.main.adapter_item_series.seriesProgress
import kotlinx.android.synthetic.main.adapter_item_series.seriesProgressBar
import kotlinx.android.synthetic.main.adapter_item_series.seriesSubtype
import kotlinx.android.synthetic.main.adapter_item_series.seriesTitle

private const val UNKNOWN_DATE = "????-??-??"

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
            .placeholder(R.drawable.ic_insert_photo)
            .error(R.drawable.ic_insert_photo)
            .into(seriesImage)
        seriesTitle.text = model.title
        seriesSubtype.text = model.subtype.name
        seriesProgress.text = containerView.context.getString(
            R.string.series_list_length,
            model.progress.toString(),
            if (model.lengthKnown) model.totalLength else '-'
        )
        setupDateString(model)
        seriesPlusOne.visibleIf(invisible = true) {
            !model.lengthKnown || model.progress < model.totalLength
        }
    }

    private fun setupDateString(model: SeriesModel) {
        val dateString = if (model.startDate.isEmpty() && model.endDate.isEmpty()) {
            containerView.context.getString(
                R.string.series_list_date_range,
                UNKNOWN_DATE,
                UNKNOWN_DATE
            )
        } else if (model.startDate == model.endDate) {
            model.startDate
        } else if (model.endDate.isEmpty()) {
            containerView.context.getString(
                R.string.series_list_date_range,
                model.startDate,
                UNKNOWN_DATE
            )
        } else {
            containerView.context.getString(
                R.string.series_list_date_range,
                model.startDate,
                model.endDate
            )
        }
        seriesDate.text = dateString
    }

    /**
     * Binds the [listener] to the view.
     */
    fun bindListener(listener: SeriesInteractionListener) {
        containerView.setOnClickListener {
            listener.seriesSelected(
                seriesImage,
                seriesModel
            )
        }
        seriesPlusOne.setOnClickListener {
            startUpdatingSeries()
            listener.onPlusOne(seriesModel) {
                finishUpdatingSeries()
            }
        }
    }

    private fun startUpdatingSeries() {
        seriesProgressBar.show()
        seriesPlusOne.isEnabled = false
        seriesPlusOne.toAlpha(0.3f)
    }

    private fun finishUpdatingSeries() {
        seriesProgressBar.hide()
        seriesPlusOne.isEnabled = true
        seriesPlusOne.toAlpha(1f)
    }
}
