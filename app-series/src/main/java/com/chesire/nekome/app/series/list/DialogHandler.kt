package com.chesire.nekome.app.series.list

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItems
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.chesire.nekome.app.series.SeriesPreferences
import com.chesire.nekome.core.R
import com.chesire.nekome.core.flags.SortOption
import com.chesire.nekome.core.flags.UserSeriesStatus
import javax.inject.Inject

/**
 * Handler to aid with displaying and interacting with Dialogs.
 */
class DialogHandler @Inject constructor(private val preferences: SeriesPreferences) {
    /**
     * Shows the filter dialog, allowing the user to choose how to filter their series.
     */
    fun showFilterDialog(context: Context, lifecycleOwner: LifecycleOwner) {
        val filterOptionMap = UserSeriesStatus
            .values()
            .filterNot { it == UserSeriesStatus.Unknown }
            .associate { context.getString(it.stringId) to it.index }

        MaterialDialog(context).show {
            title(R.string.filter_dialog_title)
            listItemsMultiChoice(
                items = filterOptionMap.keys.toList(),
                initialSelection = preferences.filterPreference.filter { it.value }.keys.toIntArray()
            ) { _, _, items: List<CharSequence> ->
                preferences.filterPreference = createFilterMap(
                    filterOptionMap,
                    items.map { it.toString() }
                )
            }
            negativeButton(R.string.filter_dialog_cancel)
            positiveButton(R.string.filter_dialog_confirm)
            lifecycleOwner(lifecycleOwner)
        }
    }

    private fun createFilterMap(
        allItems: Map<String, Int>,
        chosenItems: List<String>
    ) = mutableMapOf<Int, Boolean>().apply {
        allItems.forEach { entry ->
            this[entry.value] = chosenItems.contains(entry.key)
        }
    }

    /**
     * Shows the sort dialog, allowing the user to choose how the series list is sorted.
     */
    fun showSortDialog(context: Context, lifecycleOwner: LifecycleOwner) {
        val sortOptionMap = SortOption
            .values()
            .associate { context.getString(it.stringId) to it.index }

        MaterialDialog(context).show {
            title(R.string.sort_dialog_title)
            listItems(items = sortOptionMap.keys.toList()) { _, _, text ->
                sortOptionMap[text]?.let {
                    preferences.sortPreference = SortOption.forIndex(it)
                }
            }
            lifecycleOwner(lifecycleOwner)
        }
    }
}
