package com.chesire.nekome.app.series.list.view

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.chesire.nekome.app.series.R
import com.chesire.nekome.app.series.databinding.AdapterItemSeriesBinding
import com.chesire.nekome.app.series.list.SeriesInteractionListener
import com.chesire.nekome.app.series.list.lengthKnown
import com.chesire.nekome.core.extensions.hide
import com.chesire.nekome.core.extensions.show
import com.chesire.nekome.core.extensions.toAlpha
import com.chesire.nekome.core.extensions.visibleIf
import com.chesire.nekome.datasource.series.SeriesDomain

/**
 * ViewHolder for Series items in the Anime or Manga list.
 */
class SeriesViewHolder(
    private val binding: AdapterItemSeriesBinding
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var series: SeriesDomain

    /**
     * Binds the [model] data to the view.
     */
    fun bind(model: SeriesDomain) {
        series = model

        binding.apply {
            seriesTitle.text = model.canonicalTitle
            seriesSubtype.text = model.subtype.name
            seriesProgress.text = seriesProgress.context.getString(
                R.string.series_list_length,
                model.progress.toString(),
                if (model.lengthKnown) model.totalLength else '-'
            )
            seriesPlusOne.visibleIf(invisible = true) {
                !model.lengthKnown || model.progress < model.totalLength
            }
        }

        loadSeriesImage(model)
        loadDateString(model)
    }

    private fun loadSeriesImage(model: SeriesDomain) {
        binding.seriesImage.load(model.posterImage.smallest?.url) {
            placeholder(R.drawable.ic_insert_photo)
            error(R.drawable.ic_insert_photo)
        }
    }

    private fun loadDateString(model: SeriesDomain) = binding.seriesDate.apply {
        text = when {
            model.startDate.isEmpty() && model.endDate.isEmpty() ->
                context.getString(R.string.series_list_unknown)
            model.startDate == model.endDate -> model.startDate
            model.endDate.isEmpty() -> context.getString(
                R.string.series_list_date_range,
                model.startDate,
                context.getString(R.string.series_list_ongoing)
            )
            else -> context.getString(
                R.string.series_list_date_range,
                model.startDate,
                model.endDate
            )
        }
    }

    /**
     * Binds the [listener] to the view.
     */
    fun bindListener(listener: SeriesInteractionListener) = binding.apply {
        seriesLayout.setOnClickListener {
            listener.seriesSelected(
                seriesImage,
                series
            )
        }
        seriesPlusOne.setOnClickListener {
            startUpdatingSeries()
            listener.onPlusOne(series) {
                finishUpdatingSeries()
            }
        }
    }

    private fun startUpdatingSeries() = binding.apply {
        seriesProgressBar.show()
        seriesPlusOne.isEnabled = false
        seriesPlusOne.toAlpha(0.3f)
    }

    private fun finishUpdatingSeries() = binding.apply {
        seriesProgressBar.hide()
        seriesPlusOne.isEnabled = true
        seriesPlusOne.toAlpha(1f)
    }
}
