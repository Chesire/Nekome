package com.chesire.nekome.robots.series

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.chesire.nekome.R
import com.chesire.nekome.app.series.collection.ui.SeriesCollectionTags
import com.chesire.nekome.core.compose.composables.DialogTags
import com.chesire.nekome.helpers.getResource
import com.chesire.nekome.robots.DialogResultsRobot
import com.chesire.nekome.robots.DialogRobot

/**
 * Robot to interact with the sort dialog.
 */
class SortOptionRobot(
    private val composeContentTestRule: ComposeContentTestRule
) : DialogRobot(composeContentTestRule) {

    /**
     * Opens the sort dialog.
     */
    fun open() {
        composeContentTestRule
            .onNodeWithTag(SeriesCollectionTags.MenuSort)
            .performClick()
    }

    /**
     * Pick the "Default" option, requires first calling [open], and ending with [confirm].
     */
    fun selectByDefault() {
        composeContentTestRule
            .onNodeWithText(R.string.sort_by_default.getResource())
            .performClick()
    }

    /**
     * Pick the "Title" option, requires first calling [open], and ending with [confirm].
     */
    fun selectByTitle() {
        composeContentTestRule
            .onNodeWithText(R.string.sort_by_title.getResource())
            .performClick()
    }

    /**
     * Pick the "Start date" option, requires first calling [open], and ending with [confirm].
     */
    fun selectByStartDate() {
        composeContentTestRule
            .onNodeWithText(R.string.sort_by_start_date.getResource())
            .performClick()
    }

    /**
     * Pick the "End date" option, requires first calling [open], and ending with [confirm].
     */
    fun selectByEndDate() {
        composeContentTestRule
            .onNodeWithText(R.string.sort_by_end_date.getResource())
            .performClick()
    }

    /**
     * Pick the "Rating" option, requires first calling [open], and ending with [confirm].
     */
    fun selectByRating() {
        composeContentTestRule
            .onNodeWithText(R.string.sort_by_rating.getResource())
            .performClick()
    }

    /**
     * Executes validation steps.
     * Requires opening the dialog, performing the check.
     */
    infix fun validate(func: SortOptionResultsRobot.() -> Unit) =
        SortOptionResultsRobot(composeContentTestRule).apply(func)
}

/**
 * Robot to check the results for the sort dialog.
 */
class SortOptionResultsRobot(
    private val composeContentTestRule: ComposeContentTestRule
) : DialogResultsRobot(composeContentTestRule) {

    /**
     * Assert that the sort options are in the correct locations.
     */
    fun isLoadedCorrectly() {
        val collection = composeContentTestRule.onAllNodesWithTag(DialogTags.OptionText, true)
        collection[0].assertTextContains(R.string.sort_by_default.getResource())
        collection[1].assertTextContains(R.string.sort_by_title.getResource())
        collection[2].assertTextContains(R.string.sort_by_start_date.getResource())
        collection[3].assertTextContains(R.string.sort_by_end_date.getResource())
        collection[4].assertTextContains(R.string.sort_by_rating.getResource())
    }
}
