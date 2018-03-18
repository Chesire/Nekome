package com.chesire.malime

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.chesire.malime.models.Anime

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

    override fun getItemCount(): Int {
        return filteredItems.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindModel(filteredItems[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_malmodel, parent, false))
    }

    override fun getFilter(): Filter {
        return filter
    }

    inner class ViewHolder(
            private val animeView: View
    ) : RecyclerView.ViewHolder(animeView) {
        fun bindModel(animeModel: Anime) {
            val context = animeView.context
            val image = animeView.findViewById<ImageView>(R.id.item_malmodel_image)
            val negOneButton = animeView.findViewById<ImageButton>(R.id.item_malmodel_neg_one)
            val plusOneButton = animeView.findViewById<ImageButton>(R.id.item_malmodel_plus_one)

            GlideApp.with(animeView)
                    .load(animeModel.seriesImage)
                    .into(image)
            image.setOnClickListener({
                interactionListener.onImageClicked(animeModel)
            })
            animeView.findViewById<TextView>(R.id.item_malmodel_title).text = animeModel.seriesTitle
            animeView.findViewById<TextView>(R.id.item_malmodel_progress).text =
                    String.format(context.getString(R.string.malitem_progress_text),
                            animeModel.myWatchedEpisodes, animeModel.totalEpisodes)

            if (animeModel.seriesEpisodes == 0 || animeModel.myWatchedEpisodes != animeModel.seriesEpisodes) {
                plusOneButton.visibility = View.VISIBLE
                plusOneButton.setOnClickListener {
                    interactionListener.onPlusOneClicked(animeModel)
                }
            } else {
                plusOneButton.visibility = View.GONE
            }

            if (animeModel.myWatchedEpisodes == 0) {
                negOneButton.visibility = View.GONE
            } else {
                negOneButton.visibility = View.VISIBLE
                negOneButton.setOnClickListener {
                    interactionListener.onNegativeOneClicked(animeModel)
                }
            }
        }
    }

    private inner class AnimeFilter : Filter() {
        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            filteredItems.clear()
            if (p1?.values is ArrayList<*>) {
                filteredItems.addAll(p1.values as ArrayList<Anime>)
            }
            notifyDataSetChanged()
        }

        override fun performFiltering(p0: CharSequence?): FilterResults {
            val results = FilterResults()
            val myFilter = sharedPref.getAnimeFilter()
            val tempList = items.filter {
                // Move the compare value down to 5, so we can more easily work with it
                val compareVal = if (it.myStatus == 6) {
                    5
                } else {
                    it.myStatus
                }
                myFilter[compareVal!! - 1]
            }
            results.values = tempList
            results.count = tempList.count()

            return results
        }
    }
}