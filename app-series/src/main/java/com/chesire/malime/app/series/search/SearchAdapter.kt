package com.chesire.malime.app.series.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chesire.malime.app.series.R
import com.chesire.malime.core.models.SeriesModel

class SearchAdapter(
    private val listener: SearchInteractionListener
) : RecyclerView.Adapter<SearchViewHolder>() {
    private var searchItems = emptyList<SeriesModel>()

    fun loadItems(items: List<SeriesModel>) {
        searchItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_search, parent, false)
        )
    }

    override fun getItemCount() = searchItems.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(searchItems[position])
        holder.bindListener(listener)
    }
}
