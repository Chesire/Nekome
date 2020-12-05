package com.chesire.nekome.app.search.results

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.chesire.nekome.app.search.R
import com.chesire.nekome.app.search.domain.DiffCallback
import com.chesire.nekome.app.search.domain.SearchModel

/**
 * Adapter to aid with displaying the search results.
 */
class ResultsAdapter(
    private val resultsListener: ResultsListener
) : ListAdapter<SearchModel, ResultsViewHolder>(DiffCallback()) {

    /**
     * Stores all currently known series ids.
     */
    var storedSeriesIds: List<Int>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        return ResultsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_result, parent, false)
        ).apply {
            bindListener(resultsListener)
        }
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data, storedSeriesIds?.any { it == data.id } ?: false)
    }
}
