package com.chesire.malime.app.discover.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.chesire.malime.app.discover.R
import com.chesire.malime.core.models.SeriesModel

/**
 * Adapter for use displaying list of series when searching.
 */
class SearchAdapter(
    private val listener: SearchInteractionListener
) : ListAdapter<SeriesModel, SearchViewHolder>(SeriesModel.DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_search, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.bindListener(listener)
    }
}
