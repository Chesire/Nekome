package com.chesire.malime.view.manga

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.chesire.malime.R
import com.chesire.malime.models.Manga
import com.chesire.malime.models.UpdateManga
import com.chesire.malime.util.GlideApp
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
        val foundModelIndex = filteredItems.indexOfFirst { it.seriesMangaDbId == updatedModel.id }
        val foundModel = filteredItems[foundModelIndex]
        foundModel.myReadChapters = updatedModel.chapter
        foundModel.myReadVolumes = updatedModel.volume

        // If the state hasn't changed, just notify an item has changed, if not run the whole filter
        if (foundModel.myStatus == updatedModel.status) {
            notifyItemChanged(foundModelIndex)
        } else {
            foundModel.myStatus = updatedModel.status
            filter.filter("")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_malmodel, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return filteredItems.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindModel(filteredItems[position])
    }

    override fun getFilter(): Filter {
        return filter
    }

    inner class ViewHolder(
        private val mangaView: View
    ) : RecyclerView.ViewHolder(mangaView) {
        private val loadingLayout = mangaView.findViewById<View>(R.id.item_malmodel_loading_layout)
        private val contentLayout =
            mangaView.findViewById<ViewGroup>(R.id.item_malmodel_content_layout)

        fun bindModel(mangaModel: Manga) {
            val image = mangaView.findViewById<ImageView>(R.id.item_malmodel_image)
            val negOneButton = mangaView.findViewById<ImageButton>(R.id.item_malmodel_neg_one)
            val plusOneButton = mangaView.findViewById<ImageButton>(R.id.item_malmodel_plus_one)

            // Setup the image
            GlideApp.with(mangaView)
                .load(mangaModel.seriesImage)
                .into(image)
            image.setOnClickListener {
                interactionListener.onImageClicked(mangaModel)
            }

            // Setup the text
            mangaView.findViewById<TextView>(R.id.item_malmodel_title).text = mangaModel.seriesTitle
            mangaView.findViewById<TextView>(R.id.item_malmodel_progress).text =
                    String.format(
                        mangaView.context.getString(R.string.malitem_progress_text),
                        mangaModel.myReadChapters,
                        mangaModel.getTotalChapters()
                    )

            mangaView.setOnLongClickListener {
                showLoadingLayout(true)
                interactionListener.onLongClick(mangaModel, UpdateManga(mangaModel), {
                    showLoadingLayout(false)
                })
                true
            }

            // Setup the buttons
            if (mangaModel.seriesChapters == 0 || mangaModel.myReadChapters != mangaModel.seriesChapters) {
                plusOneButton.visibility = View.VISIBLE
                plusOneButton.setOnClickListener {
                    showLoadingLayout(true)

                    val updateModel = UpdateManga(mangaModel)
                    updateModel.chapter++
                    interactionListener.onSeriesUpdate(mangaModel, updateModel, {
                        showLoadingLayout(false)
                    })
                }
            } else {
                plusOneButton.visibility = View.GONE
            }

            if (mangaModel.myReadChapters == 0) {
                negOneButton.visibility = View.GONE
            } else {
                negOneButton.visibility = View.VISIBLE
                negOneButton.setOnClickListener {
                    showLoadingLayout(true)

                    val updateModel = UpdateManga(mangaModel)
                    updateModel.chapter--
                    interactionListener.onSeriesUpdate(mangaModel, updateModel, {
                        showLoadingLayout(false)
                    })
                }
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

    private inner class MangaFilter : Filter() {
        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            filteredItems.clear()
            if (p1?.values is List<*>) {
                filteredItems.addAll(p1.values as List<Manga>)
            }
            notifyDataSetChanged()
        }

        override fun performFiltering(p0: CharSequence?): FilterResults {
            val results = FilterResults()
            val myFilter = sharedPref.getAnimeFilter()
            val mySortOption = sharedPref.getAnimeSortOption()
            val tempList = items.filter {

                val compareVal = when {
                // Move the compare value down to 5, so we can more easily work with it
                    it.myStatus == 6 -> 5
                // If the value is 0, bump to 1
                    it.myStatus == 0 -> 1
                // Else use the default value we have
                    else -> it.myStatus
                }
                myFilter[compareVal!! - 1]
            }

            results.values = tempList.sortedWith(
                when (mySortOption) {
                    1 -> compareBy { it.seriesTitle }
                    2 -> compareBy { it.getSeriesStartDate() }
                    3 -> compareBy { it.getSeriesEndDate() }
                    else -> compareBy { it.myId }
                }
            )

            results.count = tempList.count()

            return results
        }
    }
}