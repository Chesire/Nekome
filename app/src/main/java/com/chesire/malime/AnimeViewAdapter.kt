package com.chesire.malime

import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.chesire.malime.models.Anime

class AnimeViewAdapter(
        private val items: ArrayList<Anime>,
        private val filteredItems: ArrayList<Anime>,
        private val sharedPref: SharedPref
) : RecyclerView.Adapter<AnimeViewAdapter.ViewHolder>(), Filterable {
    private val filter: AnimeFilter = AnimeFilter()

    fun addAll(newItems: List<Anime>) {
        items.clear()
        items.addAll(newItems)
        filter.filter("")
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

    class ViewHolder(
            private val animeView: View
    ) : RecyclerView.ViewHolder(animeView) {
        fun bindModel(animeModel: Anime) {
            val context = animeView.context
            GlideApp.with(animeView)
                    .load(animeModel.seriesImage)
                    .into(animeView.findViewById(R.id.item_malmodel_image))
            animeView.findViewById<CardView>(R.id.item_malmodel_card_view).setOnClickListener({
                CustomTabsIntent.Builder()
                        .build()
                        .launchUrl(context, Uri.parse(animeModel.malUrl))
            })
            animeView.findViewById<TextView>(R.id.item_malmodel_title).text = animeModel.seriesTitle
            animeView.findViewById<TextView>(R.id.item_malmodel_progress).text =
                    String.format(context.getString(R.string.malitem_progress_text),
                            animeModel.myWatchedEpisodes, animeModel.totalEpisodes)
        }
    }

    private inner class AnimeFilter : Filter() {
        private val results = FilterResults()

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            filteredItems.clear()
            if (results.values is ArrayList<*>) {
                filteredItems.addAll(results.values as ArrayList<Anime>)
            }
            notifyDataSetChanged()
        }

        override fun performFiltering(p0: CharSequence?): FilterResults {
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