package com.chesire.malime.view.manga

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.chesire.malime.models.Manga
import com.chesire.malime.models.UpdateManga
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.MalModelInteractionListener

class MangaViewAdapter(
    private val sharedPref: SharedPref,
    private val interactionListener: MalModelInteractionListener<Manga, UpdateManga>
) : RecyclerView.Adapter<MangaViewAdapter.ViewHolder>(), Filterable {
    private val items = ArrayList<Manga>()
    private val filteredItems = ArrayList<Manga>()
    private val filter: MangaViewAdapter.MangaFilter = MangaFilter()

    fun addAll(newItems: List<Manga>) {
        items.clear()
        items.addAll(newItems)
        filter.filter("")
    }

    fun getAll(): ArrayList<Manga> {
        return items
    }

    fun updateItem(updatedModel: UpdateManga) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return filteredItems.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFilter(): Filter {
        return filter
    }

    inner class ViewHolder(
        private val mangaView: View
    ) : RecyclerView.ViewHolder(mangaView)

    private inner class MangaFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}