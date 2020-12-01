package com.chesire.nekome.app.series.list.view

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.chesire.nekome.app.series.R
import com.chesire.nekome.app.series.databinding.AdapterItemSeriesBinding
import com.chesire.nekome.app.series.list.SeriesInteractionListener
import com.chesire.nekome.core.extensions.hide
import com.chesire.nekome.core.extensions.show
import com.chesire.nekome.core.extensions.toAlpha
import com.chesire.nekome.core.extensions.visibleIf
import com.chesire.nekome.core.models.SeriesModel

/**
 * ViewHolder for Series items in the Anime or Manga list.
 */
class SeriesViewHolder(private val binding: AdapterItemSeriesBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var seriesModel: SeriesModel

    /**
     * Binds the [model] data to the view.
     */
    fun bind(model: SeriesModel) {
        seriesModel = model

        binding.apply {
            seriesImage.load(model.posterImage.smallest?.url) {
                placeholder(R.drawable.ic_insert_photo)
                error(R.drawable.ic_insert_photo)
            }
            seriesTitle.text = model.title
            seriesSubtype.text = model.subtype.name
            seriesProgress.text = itemView.context.getString(
                R.string.series_list_length,
                model.progress.toString(),
                if (model.lengthKnown) model.totalLength else '-'
            )
            setupDateString(model)
            seriesPlusOne.visibleIf(invisible = true) {
                !model.lengthKnown || model.progress < model.totalLength
            }
        }
    }

    private fun setupDateString(model: SeriesModel) {
        val dateString = with(itemView.context) {
            when {
                model.startDate.isEmpty() && model.endDate.isEmpty() -> getString(R.string.series_list_unknown)
                model.startDate == model.endDate -> model.startDate
                model.endDate.isEmpty() -> getString(
                    R.string.series_list_date_range,
                    model.startDate,
                    getString(R.string.series_list_ongoing)
                )
                else -> getString(R.string.series_list_date_range, model.startDate, model.endDate)
            }
        }
        binding.seriesDate.text = dateString
    }

    /**
     * Binds the [listener] to the view.
     */
    fun bindListener(listener: SeriesInteractionListener) = binding.apply {
        root.setOnClickListener {
            listener.seriesSelected(
                binding.seriesImage,
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
