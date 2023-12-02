package com.chesire.nekome.robots.series

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import com.chesire.nekome.app.series.collection.ui.RatingTags
import com.chesire.nekome.app.series.collection.ui.SeriesCollectionTags

/**
 * Method to interact with the [SeriesListRobot].
 */
fun seriesList(
    composeContentTestRule: ComposeContentTestRule,
    func: SeriesListRobot.() -> Unit
) = SeriesListRobot(composeContentTestRule).apply(func)

/**
 * Robot to interact with the series list screen.
 */
class SeriesListRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Performs refresh on the series list.
     */
    fun refreshList() {
        composeContentTestRule
            .onNodeWithTag(SeriesCollectionTags.RefreshContainer)
            .performTouchInput {
                swipeDown()
            }
    }

    /**
     * Presses the search button.
     */
    fun goToSearch() {
        composeContentTestRule
            .onNodeWithTag(SeriesCollectionTags.SearchFab)
            .performClick()
    }

    /**
     * Presses the increment watched button on the series at position [itemPosition].
     */
    fun incrementSeriesByOne(itemPosition: Int) {
        composeContentTestRule
            .onAllNodesWithTag(SeriesCollectionTags.PlusOne)
            .get(itemPosition)
            .performClick()
    }

    /**
     * Options for changing the sort option.
     */
    fun sortSeries(func: SortOptionRobot.() -> Unit) =
        SortOptionRobot(composeContentTestRule).apply(func)

    /**
     * Options for changing the filter option.
     */
    fun filterSeries(func: FilterOptionsRobot.() -> Unit) =
        FilterOptionsRobot(composeContentTestRule).apply(func)

    /**
     * Executes validation steps.
     */
    infix fun validate(func: SeriesListResultRobot.() -> Unit) =
        SeriesListResultRobot(composeContentTestRule).apply(func)
}

/**
 * Robot to check the results for the series list screen.
 */
class SeriesListResultRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Asserts the series list screen is shown.
     */
    fun isVisible() {
        composeContentTestRule
            .onNodeWithTag(SeriesCollectionTags.Root)
            .assertIsDisplayed()
    }

    /**
     * Asserts the empty list view is displayed.
     */
    fun isEmptyDisplayed() {
        composeContentTestRule
            .onNodeWithTag(SeriesCollectionTags.EmptyView)
            .assertIsDisplayed()
    }

    /**
     * Asserts the populated list view is displayed.
     */
    fun isListDisplayed() {
        composeContentTestRule
            .onNodeWithTag(SeriesCollectionTags.RefreshContainer)
            .assertIsDisplayed()
    }

    /**
     * Asserts that [expect] numbers of items are in the list.
     */
    fun listCount(expect: Int) {
        composeContentTestRule
            .onAllNodesWithTag(SeriesCollectionTags.SeriesItem)
            .assertCountEquals(expect)
    }

    /**
     * Asserts that the refresh error toast is displayed.
     */
    fun isRefreshSeriesError() {
        composeContentTestRule
            .onNodeWithTag(SeriesCollectionTags.Snackbar)
            .assertIsDisplayed()
            .onChild()
            .onChild()
            .onChild()
            .assertTextContains(
                value = "Encountered error trying to refresh series list, please try again…",
                ignoreCase = true,
                substring = true
            )
    }

    /**
     * Asserts that the series progress at [itemPosition] is the expected value of
     * "[expectedProgress] / [totalLength]".
     */
    fun seriesProgress(itemPosition: Int, expectedProgress: Int, totalLength: Int) {
        composeContentTestRule
            .onAllNodesWithTag(SeriesCollectionTags.SeriesItem)
            .get(itemPosition)
            .assertTextContains("$expectedProgress / $totalLength")
    }

    /**
     * Asserts that the series are listed in the expected order. A list of the series titles must be
     * passed in, so that we can validate that the order is as expected.
     */
    fun isInOrder(titles: List<String>) {
        titles.forEachIndexed { index, text ->
            checkSeriesAtIndex(index, text)
        }
    }

    /**
     * Asserts that the series at index [index] contains the title [title].
     * Use this to verify that the list is in the correct order/filtered.
     */
    fun checkSeriesAtIndex(index: Int, title: String) {
        composeContentTestRule
            .onAllNodesWithTag(SeriesCollectionTags.SeriesItem)
            .get(index)
            .assertTextContains(title)
    }

    /**
     * Asserts that the rating dialog is currently displayed.
     */
    fun isRatingDialogDisplayed() {
        composeContentTestRule
            .onNodeWithTag(RatingTags.Root)
            .assertIsDisplayed()
    }

    /**
     * Asserts that the rating dialog is not currently displayed.
     */
    fun isRatingDialogNotDisplayed() {
        try {
            composeContentTestRule
                .onNodeWithTag(RatingTags.Root)
                .assertIsNotDisplayed()
        } catch (ex: AssertionError) {
            // If an ex is thrown, then the node wasn't available
        }
    }
}
