package com.chesire.nekome.app.series.list

import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItems
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.chesire.nekome.app.series.R
import com.chesire.nekome.app.series.SeriesPreferences
import com.chesire.nekome.core.flags.SortOption
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.google.android.material.slider.Slider

/**
 * Shows the filter dialog, allowing the user to choose how to filter their series list.
 */
fun Fragment.showFilterDialog(preferences: SeriesPreferences) {
    val context = context ?: return
    val filterOptionMap = UserSeriesStatus.getValueMap(context)

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

/**
 * Shows the rating dialog, allowing the user to choose a rating for the series. On pressing confirm
 * the rating value is sent back in [onFinish], if it is cancelled then the value 0 is sent instead.
 */
fun Fragment.showRateDialog(onFinish: (Int) -> Unit) {
    val context = context ?: return

    var slider: Slider
    MaterialDialog(context).show {
        title(R.string.series_list_rate_title)
        customView(R.layout.view_rate_series).apply {
            slider = findViewById(R.id.ratingSlider)
        }
        positiveButton(R.string.series_list_rate_confirm) { onFinish(slider.value.toInt()) }
        negativeButton(R.string.series_list_rate_cancel) { onFinish(0) }
        onCancel { onFinish(0) }
        lifecycleOwner(viewLifecycleOwner)
    }
}

private fun createFilterMap(allItems: Map<Int, String>, chosenItems: List<String>) =
    mutableMapOf<Int, Boolean>().apply {
        allItems.forEach { entry ->
            this[entry.key] = chosenItems.contains(entry.value)
        }
    }
