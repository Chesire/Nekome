package com.chesire.malime.app.search.results

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.chesire.malime.app.search.R
import com.chesire.malime.core.models.SeriesModel

/**
 * Adapter to aid with displaying the search results.
 */
class ResultsAdapter(
    private val trackSeriesAction: (SeriesModel) -> Unit
) : ListAdapter<SeriesModel, ResultsViewHolder>(SeriesModel.DiffCallback()) {

    /**
     * Stores all currently known series.
     */
    var allSeries: List<SeriesModel>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        return ResultsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_result, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data, allSeries?.any { it.id == data.id } ?: false)
        holder.bindAction(trackSeriesAction)
    }
}
