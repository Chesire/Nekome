package com.chesire.malime.view.search

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spanned
import android.text.SpannedString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.chesire.malime.R
import com.chesire.malime.models.Anime
import com.chesire.malime.models.Entry
import com.chesire.malime.models.Manga
import com.chesire.malime.util.GlideApp

class SearchViewAdapter(
    private val interactionListener: SearchInteractionListener
) : RecyclerView.Adapter<SearchViewAdapter.ViewHolder>(), Filterable {
    private val items = ArrayList<Entry>()
    private val filteredItems = ArrayList<Entry>()
    private val currentAnime: ArrayList<Anime> = ArrayList()
    private var currentAnimeIds: Set<Int?> = HashSet()
    private val currentManga: ArrayList<Manga> = ArrayList()
    private var currentMangaIds: Set<Int?> = HashSet()
    private val filter: SearchFilter = SearchFilter()

    fun getAll(): ArrayList<Entry> {
        return items
    }

    fun getCurrentAnime(): ArrayList<Anime> {
        return currentAnime
    }

    fun getCurrentManga(): ArrayList<Manga> {
        return currentManga
    }

    fun setCurrentAnime(animeList: List<Anime>) {
        currentAnime.clear()
        currentAnime.addAll(animeList)
        currentAnimeIds = currentAnime.map { it.seriesAnimeDbId }.toSet()
    }

    fun setCurrentManga(mangaList: List<Manga>) {
        currentManga.clear()
        currentManga.addAll(mangaList)
        currentMangaIds = currentManga.map { it.seriesMangaDbId }.toSet()
    }

    fun update(newItems: List<Entry>) {
        items.clear()
        items.addAll(newItems)
        filter.filter("")
    }

    override fun getFilter(): Filter {
        return filter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return filteredItems.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindModel(filteredItems[position])
    }

    inner class ViewHolder(
        private val searchView: View
    ) : RecyclerView.ViewHolder(searchView) {
        private val loadingLayout = searchView.findViewById<View>(R.id.item_search_loading_layout)
        private val contentLayout =
            searchView.findViewById<ViewGroup>(R.id.item_search_content_layout)

        fun bindModel(entryModel: Entry) {
            val image = searchView.findViewById<ImageView>(R.id.item_search_image)

            // Setup the image
            GlideApp.with(searchView)
                .load(entryModel.image)
                .into(image)

            // Setup the text
            searchView.findViewById<TextView>(R.id.item_search_title).text = entryModel.title
            searchView.findViewById<TextView>(R.id.item_search_progress).text =
                    fromHtml(entryModel.synopsis)

            searchView.findViewById<ImageButton>(R.id.search_image_add_button).setOnClickListener {
                interactionListener.onAddPressed(entryModel)
            }
        }

        private fun fromHtml(input: String?): Spanned {
            return when {
                input == null -> SpannedString("")
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> Html.fromHtml(
                    input,
                    Html.FROM_HTML_MODE_LEGACY
                )
                else -> Html.fromHtml(input)
            }
        }
    }

    private inner class SearchFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val knownIds = if (items.first().chapters == null) {
                currentAnimeIds
            } else {
                currentMangaIds
            }

            val tempList = items.filterNot { it.id in knownIds }
            results.values = tempList
            results.count = tempList.count()

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredItems.clear()
            if (results?.values is List<*>) {
                filteredItems.addAll(results.values as List<Entry>)
            }
            notifyDataSetChanged()
        }

    }
}