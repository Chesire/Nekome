package com.chesire.malime.flow.series.list.anime

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chesire.malime.R
import com.chesire.malime.core.models.SeriesModel

class AnimeAdapter(
    private val listener: AnimeInteractionListener
) : RecyclerView.Adapter<AnimeViewHolder>() {
    var animeItems = emptyList<SeriesModel>()

    fun loadItems(items: List<SeriesModel>) {
        animeItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        return AnimeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_anime, parent, false)
        )
    }

    override fun getItemCount() = animeItems.size

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bind(animeItems[position])
        holder.bindListener(listener)
    }
}
