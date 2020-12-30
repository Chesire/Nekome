package com.chesire.nekome.app.series.list

import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItems
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.chesire.nekome.app.series.SeriesPreferences
import com.chesire.nekome.app.series.extensions.UserSeriesStatusExtensions
import com.chesire.nekome.core.R
import com.chesire.nekome.core.flags.SortOption

/**
 * Shows the filter dialog, allowing the user to choose how to filter their series list.
 */
fun Fragment.showFilterDialog(preferences: SeriesPreferences) {
    val context = context ?: return
    val filterOptionMap = UserSeriesStatusExtensions.getValueMap(context)

    MaterialDialog(context).show {
        title(R.string.filter_dialog_title)
        listItemsMultiChoice(
            items = filterOptionMap.values.toList(),
            initialSelection = preferences.filterPreference.filter { it.value }.keys.toIntArray()
        ) { _, _, items ->
            preferences.filterPreference = createFilterMap(
                filterOptionMap,
                items.map { it.toString() }
            )
        }
        negativeButton(R.string.filter_dialog_cancel)
        positiveButton(R.string.filter_dialog_confirm)
        lifecycleOwner(viewLifecycleOwner)
    }
}

/**
 * Shows the sort dialog, allowing the user to choose how the series list is sorted.
 */
fun Fragment.showSortDialog(preferences: SeriesPreferences) {
    val context = context ?: return
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
        lifecycleOwner(viewLifecycleOwner)
    }
}

private fun createFilterMap(allItems: Map<Int, String>, chosenItems: List<String>) =
    mutableMapOf<Int, Boolean>().apply {
        allItems.forEach { entry ->
            this[entry.key] = chosenItems.contains(entry.value)
        }
    }
