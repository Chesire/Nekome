package com.chesire.nekome.robots.series

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.chesire.nekome.R
import com.chesire.nekome.helpers.ToastMatcher.Companion.onToast
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItemChild
import com.schibsted.spain.barista.interaction.BaristaSwipeRefreshInteractions.refresh

/**
 * Method to interact with the [SeriesListRobot].
 */
fun seriesList(func: SeriesListRobot.() -> Unit) = SeriesListRobot().apply { func() }

/**
 * Robot to interact with the series list screen.
 */
class SeriesListRobot {

    /**
     * Performs refresh on the series list.
     */
    fun refreshList() {
        refresh(R.id.refreshLayout)
    }

    /**
     * Presses the increment watched button on the series at position [itemPosition].
     */
    fun incrementSeriesByOne(itemPosition: Int) =
        clickListItemChild(R.id.listContent, itemPosition, R.id.seriesPlusOne)

    /**
     * Executes validation steps.
     */
    infix fun validate(func: SeriesListResultRobot.() -> Unit): SeriesListResultRobot {
        return SeriesListResultRobot().apply { func() }
    }
}

/**
 * Robot to check the results for the series list screen.
 */
class SeriesListResultRobot {

    /**
     * Asserts the series list screen is shown.
     */
    fun isVisible() = assertDisplayed(R.id.seriesListLayout)

    /**
     * Asserts the empty list view is displayed.
     */
    fun isEmptyDisplayed() = assertDisplayed(R.id.listEmpty)

    /**
     * Asserts the populated list view is displayed.
     */
    fun isListDisplayed() = assertDisplayed(R.id.seriesLayout)

    /**
     * Asserts that [expect] numbers of items are in the list.
     */
    fun listCount(expect: Int) = assertRecyclerViewItemCount(R.id.listContent, expect)

    /**
     * Asserts that the refresh error toast is displayed.
     */
    fun isRefreshSeriesError() {
        onToast(R.string.series_list_refresh_error).check(matches(isDisplayed()))
    }

    /**
     * Asserts that the series progress at [itemPosition] is the expected value of
     * "[expectedProgress] / [totalLength]".
     */
    fun seriesProgress(itemPosition: Int, expectedProgress: Int, totalLength: Int) =
        assertDisplayedAtPosition(
            R.id.listContent,
            itemPosition,
            R.id.seriesProgress,
            "$expectedProgress / $totalLength"
        )
}
