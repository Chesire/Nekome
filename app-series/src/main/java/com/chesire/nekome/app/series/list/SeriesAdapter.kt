package com.chesire.nekome.app.series.list

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chesire.nekome.app.series.R
import com.chesire.nekome.app.series.SharedPref
import com.chesire.nekome.core.flags.SortOption
import com.chesire.nekome.core.models.SeriesModel
import timber.log.Timber

/**
 * Adapter for use displaying the series.
 */
class SeriesAdapter(
    private val listener: SeriesInteractionListener,
    private val sharedPref: SharedPref
) : ListAdapter<SeriesModel, SeriesViewHolder>(SeriesModel.DiffCallback()),
    SharedPreferences.OnSharedPreferenceChangeListener {

    init {
        sharedPref.subscribeToChanges(this)
    }

    private var container: RecyclerView? = null
    private var completeList = listOf<SeriesModel>()

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

    override fun submitList(list: List<SeriesModel>?) {
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
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_series, parent, false)
        ).apply {
            bindListener(listener)
        }
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) =
        holder.bind(getItem(position))

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
