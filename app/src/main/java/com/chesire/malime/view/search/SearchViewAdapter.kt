package com.chesire.malime.view.search

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spanned
import android.text.SpannedString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
) : RecyclerView.Adapter<SearchViewAdapter.ViewHolder>() {
    private val items = ArrayList<Entry>()
    private val currentAnime = ArrayList<Anime>()
    private val currentManga = ArrayList<Manga>()
    private var currentAnimeIds: List<Int?> = ArrayList()
    private var currentMangaIds: List<Int?> = ArrayList()

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
        currentAnimeIds = currentAnime.map { it.seriesAnimeDbId }.toList()
    }

    fun setCurrentManga(mangaList: List<Manga>) {
        currentManga.clear()
        currentManga.addAll(mangaList)
        currentMangaIds = currentManga.map { it.seriesMangaDbId }.toList()
    }

    fun update(newItems: List<Entry>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindModel(items[position])
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
            image.setOnClickListener {
                interactionListener.onImageClicked(entryModel)
            }

            // Setup the text
            searchView.findViewById<TextView>(R.id.item_search_title).text = entryModel.title
            searchView.findViewById<TextView>(R.id.item_search_progress).text =
                    fromHtml(entryModel.synopsis)

            val addButton = searchView.findViewById<ImageButton>(R.id.search_image_add_button)
            val knownIds = if (items.first().chapters == null) {
                currentAnimeIds
            } else {
                currentMangaIds
            }

            if (knownIds.contains(entryModel.id)) {
                addButton.visibility = View.INVISIBLE
            } else {
                addButton.setOnClickListener {
                    showLoadingLayout(true)
                    interactionListener.onAddPressed(entryModel, { success ->
                        showLoadingLayout(false)

                        if (success) {
                            addButton.visibility = View.INVISIBLE

                            if (entryModel.chapters == null) {
                                (currentAnimeIds as ArrayList).add(entryModel.id)
                            } else {
                                (currentMangaIds as ArrayList).add(entryModel.id)
                            }
                        }
                    })
                }
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

        private fun showLoadingLayout(displayLoader: Boolean) {
            loadingLayout.visibility = if (displayLoader) {
                View.VISIBLE
            } else {
                View.GONE
            }

            contentLayout.isEnabled = !displayLoader
            for (i in 0 until contentLayout.childCount) {
                contentLayout.getChildAt(i).isEnabled = !displayLoader
            }
        }
    }

}