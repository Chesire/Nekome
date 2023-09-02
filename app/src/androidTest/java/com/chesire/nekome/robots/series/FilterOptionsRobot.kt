package com.chesire.nekome.robots.series

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.chesire.nekome.app.series.collection.ui.FilterTags
import com.chesire.nekome.app.series.collection.ui.SeriesCollectionTags
import com.chesire.nekome.helpers.getResource
import com.chesire.nekome.resources.StringResource

/**
 * Robot to interact with the filter dialog.
 */
class FilterOptionsRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Opens the filter dialog.
     */
    fun open() {
        composeContentTestRule
            .onNodeWithTag(SeriesCollectionTags.MenuFilter)
            .performClick()
    }

    /**
     * Pick the "Current" option, requires first calling [open].
     */
    fun clickCurrent() {
        composeContentTestRule
            .onNodeWithText(StringResource.filter_by_current.getResource())
            .performClick()
    }

    /**
     * Pick the "Completed" option, requires first calling [open].
     */
    fun clickCompleted() {
        composeContentTestRule
            .onNodeWithText(StringResource.filter_by_completed.getResource())
            .performClick()
    }

    /**
     * Pick the "On hold" option, requires first calling [open].
     */
    fun clickOnHold() {
        composeContentTestRule
            .onNodeWithText(StringResource.filter_by_on_hold.getResource())
            .performClick()
    }

    /**
     * Pick the "Dropped" option, requires first calling [open].
     */
    fun clickDropped() {
        composeContentTestRule
            .onNodeWithText(StringResource.filter_by_dropped.getResource())
            .performClick()
    }

    /**
     * Pick the "Planned" option, requires first calling [open].
     */
    fun clickPlanned() {
        composeContentTestRule
            .onNodeWithText(StringResource.filter_by_planned.getResource())
            .performClick()
    }

    /**
     * Clicks the confirm dialog option, requires first calling [open].
     */
    fun confirm() {
        composeContentTestRule
            .onNodeWithTag(FilterTags.OkButton)
            .performClick()
    }

    /**
     * Clicks the cancel dialog option, requires first calling [open].
     */
    fun cancel() {
        composeContentTestRule
            .onNodeWithTag(FilterTags.CancelButton)
            .performClick()
    }

    /**
     * Executes validation steps.
     * Requires opening the dialog, performing the check.
     */
    infix fun validate(func: FilterOptionsResultsRobot.() -> Unit) =
        FilterOptionsResultsRobot(composeContentTestRule).apply(func)
}

/**
 * Robot to check the results for the filter dialog.
 */
class FilterOptionsResultsRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Assert that the filter options dialog is visible.
     */
    fun isVisible() {
        composeContentTestRule
            .onNodeWithTag(FilterTags.Root)
            .assertIsDisplayed()
    }

    /**
     * Assert that the filter options dialog is not visible.
     */
    fun isNotVisible() {
        try {
            composeContentTestRule
                .onNodeWithTag(FilterTags.Root)
                .assertIsNotDisplayed()
        } catch (ex: AssertionError) {
            // If an ex is thrown, then the node wasn't available
        }
    }

    /**
     * Assert that the filter options are in the correct locations.
     */
    fun isLoadedCorrectly() {
        val collection = composeContentTestRule.onAllNodesWithTag(FilterTags.OptionText, true)
        collection[0].assertTextContains(StringResource.filter_by_current.getResource())
        collection[1].assertTextContains(StringResource.filter_by_completed.getResource())
        collection[2].assertTextContains(StringResource.filter_by_on_hold.getResource())
        collection[3].assertTextContains(StringResource.filter_by_dropped.getResource())
        collection[4].assertTextContains(StringResource.filter_by_planned.getResource())
    }

    /**
     * Assert that the filter dialog options are in the default order.
     */
    fun isInDefaultState() {
        isCurrentChecked()
        isCompletedNotChecked()
        isOnHoldNotChecked()
        isDroppedNotChecked()
        isPlannedNotChecked()
    }

    /**
     * Assert that the confirm dialog option is enabled.
     */
    fun confirmIsEnabled() {
        composeContentTestRule
            .onNodeWithTag(FilterTags.OkButton)
            .assertIsEnabled()
    }

    /**
     * Assert that the confirm dialog option is disabled.
     */
    fun confirmIsDisabled() {
        composeContentTestRule
            .onNodeWithTag(FilterTags.OkButton)
            .assertIsNotEnabled()
    }

    /**
     * Assert that the "Current" choice is checked.
     */
    fun isCurrentChecked() {
        composeContentTestRule
            .onAllNodesWithTag(FilterTags.OptionChecked, true)
            .get(0)
            .assertIsOn()
    }

    /**
     * Assert that the "Current" choice is not checked.
     */
    fun isCurrentNotChecked() {
        composeContentTestRule
            .onAllNodesWithTag(FilterTags.OptionChecked, true)
            .get(0)
            .assertIsOff()
    }

    /**
     * Assert that the "Completed" choice is checked.
     */
    fun isCompletedChecked() {
        composeContentTestRule
            .onAllNodesWithTag(FilterTags.OptionChecked, true)
            .get(1)
            .assertIsOn()
    }

    /**
     * Assert that the "Completed" choice is not checked.
     */
    fun isCompletedNotChecked() {
        composeContentTestRule
            .onAllNodesWithTag(FilterTags.OptionChecked, true)
            .get(1)
            .assertIsToggleable()
            .assertIsOff()
    }

    /**
     * Assert that the "On hold" choice is checked.
     */
    fun isOnHoldChecked() {
        composeContentTestRule
            .onAllNodesWithTag(FilterTags.OptionChecked, true)
            .get(2)
            .assertIsOn()
    }

    /**
     * Assert that the "On hold" choice is not checked.
     */
    fun isOnHoldNotChecked() {
        composeContentTestRule
            .onAllNodesWithTag(FilterTags.OptionChecked, true)
            .get(2)
            .assertIsOff()
    }

    /**
     * Assert that the "Dropped" choice is checked.
     */
    fun isDroppedChecked() {
        composeContentTestRule
            .onAllNodesWithTag(FilterTags.OptionChecked, true)
            .get(3)
            .assertIsOn()
    }

    /**
     * Assert that the "Dropped" choice is not checked.
     */
    fun isDroppedNotChecked() {
        composeContentTestRule
            .onAllNodesWithTag(FilterTags.OptionChecked, true)
            .get(3)
            .assertIsOff()
    }

    /**
     * Assert that the "Planned" choice is checked.
     */
    fun isPlannedChecked() {
        composeContentTestRule
            .onAllNodesWithTag(FilterTags.OptionChecked, true)
            .get(4)
            .assertIsOn()
    }

    /**
     * Assert that the "Planned" choice is not checked.
     */
    fun isPlannedNotChecked() {
        composeContentTestRule
            .onAllNodesWithTag(FilterTags.OptionChecked, true)
            .get(4)
            .assertIsOff()
    }
}
