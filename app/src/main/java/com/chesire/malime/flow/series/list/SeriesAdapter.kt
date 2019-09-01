package com.chesire.malime.flow.series.list

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.chesire.malime.R
import com.chesire.malime.core.SharedPref
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.core.flags.SortOption
import timber.log.Timber

/**
 * Adapter for use displaying the series.
 */
class SeriesAdapter(
    private val listener: SeriesInteractionListener,
    private val sharedPref: com.chesire.malime.core.SharedPref
) : ListAdapter<SeriesModel, SeriesViewHolder>(SeriesModelDiffCallback()),
    SharedPreferences.OnSharedPreferenceChangeListener {

    init {
        sharedPref.subscribeToChanges(this)
    }

    private var completeList: MutableList<SeriesModel> = mutableListOf()

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
            com.chesire.malime.core.SharedPref.FILTER_PREFERENCE, com.chesire.malime.core.SharedPref.SORT_PREFERENCE -> submitList(completeList)
        }
    }

    private fun executeFilter(items: List<SeriesModel>) = items.filter {
        sharedPref.filterPreference[it.userSeriesStatus.index] ?: false
    }

    private fun executeSort(items: List<SeriesModel>) = items.sortedWith(
        when (sharedPref.sortPreference) {
            com.chesire.malime.core.flags.SortOption.Default -> compareBy { it.userId }
            com.chesire.malime.core.flags.SortOption.Title -> compareBy { it.title }
            com.chesire.malime.core.flags.SortOption.StartDate -> compareBy { it.startDate }
            com.chesire.malime.core.flags.SortOption.EndDate -> compareBy { it.endDate }
        }
    )
}
