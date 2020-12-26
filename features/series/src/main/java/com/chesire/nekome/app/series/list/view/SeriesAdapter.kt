package com.chesire.nekome.app.series.list.view

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chesire.nekome.app.series.SeriesPreferences
import com.chesire.nekome.app.series.databinding.AdapterItemSeriesBinding
import com.chesire.nekome.app.series.list.SeriesInteractionListener
import com.chesire.nekome.core.flags.SortOption
import com.chesire.nekome.library.SeriesDomain
import timber.log.Timber

/**
 * Adapter for use displaying the series.
 */
class SeriesAdapter(
    private val listener: SeriesInteractionListener,
    private val seriesPreferences: SeriesPreferences
) : ListAdapter<SeriesDomain, SeriesViewHolder>(SeriesDomainDiffCallback()),
    SharedPreferences.OnSharedPreferenceChangeListener {

    init {
        seriesPreferences.subscribeToChanges(this)
    }

    private var container: RecyclerView? = null
    private var completeList = listOf<SeriesDomain>()

    /**
     * Execute when an item has been swiped away in the adapter.
     */
    fun attemptDeleteItem(position: Int) {
        val model = getItem(position)
        listener.seriesDelete(model) { confirmed ->
            container?.findViewHolderForAdapterPosition(position)?.let { viewHolder ->
                if (confirmed) {
                    submitList(completeList.minus(model).toMutableList())
                } else {
                    notifyDataSetChanged()
                    viewHolder.itemView.alpha = 1f
                }
            } ?: notifyDataSetChanged() // just call notifyDataSetChanged to reset some state
        }
    }

    override fun submitList(list: List<SeriesDomain>?) {
        if (list == null) {
            Timber.w("Null list attempted to be passed to submitList")
            super.submitList(list)
        } else {
            completeList = list
            super.submitList(executeSort(executeFilter(list)))
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        container = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        container = null
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        return SeriesViewHolder(
            AdapterItemSeriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ).apply {
            bindListener(listener)
        }
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            SeriesPreferences.FILTER_PREFERENCE, SeriesPreferences.SORT_PREFERENCE -> submitList(
                completeList
            )
        }
    }

    private fun executeFilter(items: List<SeriesDomain>) = items.filter {
        seriesPreferences.filterPreference[it.userSeriesStatus.index] ?: false
    }

    private fun executeSort(items: List<SeriesDomain>) = items.sortedWith(
        when (seriesPreferences.sortPreference) {
            SortOption.Default -> compareBy { it.userId }
            SortOption.Title -> compareBy { it.canonicalTitle }
            SortOption.StartDate -> compareBy { it.startDate }
            SortOption.EndDate -> compareBy { it.endDate }
        }
    )
}
