package com.chesire.malime.view.anime

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
import com.chesire.malime.models.Anime
import com.chesire.malime.models.UpdateAnime
import com.chesire.malime.util.GlideApp
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.MalModelInteractionListener

class AnimeViewAdapter(
    private val items: ArrayList<Anime>,
    private val filteredItems: ArrayList<Anime>,
    private val sharedPref: SharedPref,
    private val interactionListener: MalModelInteractionListener<Anime>
) : RecyclerView.Adapter<AnimeViewAdapter.ViewHolder>(), Filterable {
    private val filter: AnimeFilter = AnimeFilter()

    fun addAll(newItems: List<Anime>) {
        items.clear()
        items.addAll(newItems)
        filter.filter("")
    }

    fun getAll(): ArrayList<Anime> {
        return items
    }

    fun updateItem(updatedModel: UpdateAnime) {
        val foundModelIndex = filteredItems.indexOfFirst { it.seriesAnimeDbId == updatedModel.id }
        val foundModel = filteredItems[foundModelIndex]
        foundModel.myWatchedEpisodes = updatedModel.episode

        // If the state hasn't changed, just notify an item has changed, if not run the whole filter
        if (foundModel.myStatus == updatedModel.status) {
            notifyItemChanged(foundModelIndex)
        } else {
            foundModel.myStatus = updatedModel.status
            filter.filter("")
        }
    }

    override fun getItemCount(): Int {
        return filteredItems.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindModel(filteredItems[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_malmodel, parent, false)
        )
    }

    override fun getFilter(): Filter {
        return filter
    }

    inner class ViewHolder(
        private val animeView: View
    ) : RecyclerView.ViewHolder(animeView) {
        private val loadingLayout = animeView.findViewById<View>(R.id.item_malmodel_loading_layout)
        private val contentLayout =
            animeView.findViewById<ViewGroup>(R.id.item_malmodel_content_layout)

        fun bindModel(animeModel: Anime) {
            val image = animeView.findViewById<ImageView>(R.id.item_malmodel_image)
            val negOneButton = animeView.findViewById<ImageButton>(R.id.item_malmodel_neg_one)
            val plusOneButton = animeView.findViewById<ImageButton>(R.id.item_malmodel_plus_one)

            // Setup the image
            GlideApp.with(animeView)
                .load(animeModel.seriesImage)
                .into(image)
            image.setOnClickListener({
                interactionListener.onImageClicked(animeModel)
            })

            // Setup the text
            animeView.findViewById<TextView>(R.id.item_malmodel_title).text = animeModel.seriesTitle
            animeView.findViewById<TextView>(R.id.item_malmodel_progress).text =
                    String.format(
                        animeView.context.getString(R.string.malitem_progress_text),
                        animeModel.myWatchedEpisodes,
                        animeModel.totalEpisodes
                    )

            // Setup the buttons
            if (animeModel.seriesEpisodes == 0 || animeModel.myWatchedEpisodes != animeModel.seriesEpisodes) {
                plusOneButton.visibility = View.VISIBLE
                plusOneButton.setOnClickListener {
                    showLoadingLayout(true)
                    interactionListener.onPlusOneClicked(animeModel, {
                        showLoadingLayout(false)
                    })
                }
            } else {
                plusOneButton.visibility = View.GONE
            }

            if (animeModel.myWatchedEpisodes == 0) {
                negOneButton.visibility = View.GONE
            } else {
                negOneButton.visibility = View.VISIBLE
                negOneButton.setOnClickListener {
                    showLoadingLayout(true)
                    interactionListener.onNegativeOneClicked(animeModel, {
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

    private inner class AnimeFilter : Filter() {
        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            filteredItems.clear()
            if (p1?.values is List<*>) {
                filteredItems.addAll(p1.values as List<Anime>)
            }
            notifyDataSetChanged()
        }

        override fun performFiltering(p0: CharSequence?): FilterResults {
            val results = FilterResults()
            val myFilter = sharedPref.getAnimeFilter()
            val mySortOption = sharedPref.getAnimeSortOption()
            val tempList = items.filter {
                // Move the compare value down to 5, so we can more easily work with it
                val compareVal = if (it.myStatus == 6) {
                    5
                } else {
                    it.myStatus
                }
                myFilter[compareVal!! - 1]
            }

            results.values = tempList.sortedWith(
                when (mySortOption) {
                    1 -> compareBy { it.seriesTitle }
                    2 -> compareBy { it.seriesStartDate }
                    3 -> compareBy { it.seriesEndDate }
                    else -> compareBy { it.myId }
                }
            )

            results.count = tempList.count()

            return results
        }
    }
}