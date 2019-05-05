package com.chesire.malime.flow.series.list.anime

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chesire.malime.R
import com.chesire.malime.SharedPref
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.flow.series.SortOption

class AnimeAdapter(
    private val listener: AnimeInteractionListener,
    private val sharedPref: SharedPref
) : RecyclerView.Adapter<AnimeViewHolder>() {

    private var displayedItems = emptyList<SeriesModel>()

    /**
     * List of all series items.
     */
    var allItems: List<SeriesModel> = emptyList()
        set(value) {
            field = value
            performFilter()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        return AnimeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_anime, parent, false)
        )
    }

    override fun getItemCount() = displayedItems.size

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bind(displayedItems[position])
        holder.bindListener(listener)
    }

    /**
     * Filters the displayed values based on the value set into the shared preferences. After
     * performing the filter it will then sort the values based on the sort option.
     */
    fun performFilter() {
        val filterOptions = sharedPref.filterPreference
        displayedItems = allItems.filter {
            filterOptions[it.userSeriesStatus.index] ?: false
        }

        // Sort all the new entries again
        performSort()
    }

    /**
     * Sorts the displayed items based on the value set into the shared preferences.
     */
    fun performSort() {
        displayedItems = displayedItems
            .sortedWith(
                when (sharedPref.sortPreference) {
                    SortOption.Default -> compareBy { it.userId }
                    SortOption.Title -> compareBy { it.title }
                    SortOption.StartDate -> compareBy { it.startDate }
                    SortOption.EndDate -> compareBy { it.endDate }
                }
            )

        notifyDataSetChanged()
    }
}
