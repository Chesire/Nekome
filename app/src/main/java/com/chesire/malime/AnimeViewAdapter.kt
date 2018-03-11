package com.chesire.malime

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import com.chesire.malime.models.Anime

class AnimeViewAdapter(
        private val items: ArrayList<Anime>
) : RecyclerView.Adapter<AnimeViewAdapter.ViewHolder>() {
    fun addAll(newItems: List<Anime>) {
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        TODO("not implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("not implemented")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("not implemented")
    }

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
}