package com.chesire.malime.flow.series.list

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.chesire.malime.R
import com.chesire.malime.core.SharedPref
import com.chesire.malime.core.flags.SortOption
import com.chesire.malime.core.models.SeriesModel
import timber.log.Timber

/**
 * Adapter for use displaying the series.
 */
class SeriesAdapter(
    private val listener: SeriesInteractionListener,
    private val sharedPref: SharedPref
) : ListAdapter<SeriesModel, SeriesViewHolder>(SeriesModelDiffCallback()),
    SharedPreferences.OnSharedPreferenceChangeListener {

    init {
        sharedPref.subscribeToChanges(this)
    }

    private var completeList = mutableListOf<SeriesModel>()

    /**
     * Execute when an item has been swiped away in the adapter.
     */
    fun attemptDeleteItem(position: Int, callback: (Boolean) -> Unit) {
        listener.seriesDelete(getItem(position), position) { confirmed ->
            if (!confirmed) {
                notifyDataSetChanged()
            }
            callback(confirmed)
        }
    }

    override fun submitList(list: MutableList<SeriesModel>?) {
        if (list == null) {
            Timber.w("Null list attempted to be passed to submitList")
            super.submitList(list)
        } else {
            completeList = list
            super.submitList(executeSort(executeFilter(list)))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        return SeriesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_series, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.bindListener(listener)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            SharedPref.FILTER_PREFERENCE, SharedPref.SORT_PREFERENCE -> submitList(completeList)
        }
    }

    private fun executeFilter(items: List<SeriesModel>) = items.filter {
        sharedPref.filterPreference[it.userSeriesStatus.index] ?: false
    }

    private fun executeSort(items: List<SeriesModel>) = items.sortedWith(
        when (sharedPref.sortPreference) {
            SortOption.Default -> compareBy { it.userId }
            SortOption.Title -> compareBy { it.title }
            SortOption.StartDate -> compareBy { it.startDate }
            SortOption.EndDate -> compareBy { it.endDate }
        }
    )
}
