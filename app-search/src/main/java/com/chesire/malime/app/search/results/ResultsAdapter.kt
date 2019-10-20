package com.chesire.malime.app.search.results

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.chesire.malime.app.search.R
import com.chesire.malime.core.models.SeriesModel

/**
 * Adapter to aid with displaying the search results.
 */
class ResultsAdapter : ListAdapter<SeriesModel, ResultsViewHolder>(SeriesModel.DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        return ResultsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_result, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
