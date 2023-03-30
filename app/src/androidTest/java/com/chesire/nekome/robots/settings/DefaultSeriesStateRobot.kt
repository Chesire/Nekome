package com.chesire.nekome.robots.settings

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.chesire.nekome.core.compose.composables.DialogTags
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.helpers.getResource
import com.chesire.nekome.robots.DialogResultsRobot
import com.chesire.nekome.robots.DialogRobot

/**
 * Robot to interact with the default series state dialog.
 */
class DefaultSeriesStateRobot(
    private val composeContentTestRule: ComposeContentTestRule
) : DialogRobot(composeContentTestRule) {

    /**
     * Picks the "Current" option.
     */
    fun chooseCurrent() {
        composeContentTestRule
            .onNodeWithText(UserSeriesStatus.Current.stringId.getResource())
            .performClick()
    }

    /**
     * Picks the "Completed" option.
     */
    fun chooseCompleted() {
        composeContentTestRule
            .onNodeWithText(UserSeriesStatus.Completed.stringId.getResource())
            .performClick()
    }

    /**
     * Picks the "On hold" option.
     */
    fun chooseOnHold() {
        composeContentTestRule
            .onNodeWithText(UserSeriesStatus.OnHold.stringId.getResource())
            .performClick()
    }

    /**
     * Picks the "Dropped" option.
     */
    fun chooseDropped() {
        composeContentTestRule
            .onNodeWithText(UserSeriesStatus.Dropped.stringId.getResource())
            .performClick()
    }

    /**
     * Picks the "Planned" option.
     */
    fun choosePlanned() {
        composeContentTestRule
            .onNodeWithText(UserSeriesStatus.Planned.stringId.getResource())
            .performClick()
    }

    /**
     * Executes validation steps.
     * Requires opening the dialog, performing the check, then closing the dialog again.
     */
    infix fun validate(func: DefaultSeriesStateResultRobot.() -> Unit) =
        DefaultSeriesStateResultRobot(composeContentTestRule).apply(func)
}

/**
 * Robot to check the results for the default series state dialog.
 */
class DefaultSeriesStateResultRobot(
    private val composeContentTestRule: ComposeContentTestRule
) : DialogResultsRobot(composeContentTestRule) {

    /**
     * Assert that the options are in the correct locations.
     */
    fun isLoadedCorrectly() {
        val collection = composeContentTestRule.onAllNodesWithTag(DialogTags.OptionText, true)
        collection[0].assertTextContains(UserSeriesStatus.Current.stringId.getResource())
        collection[1].assertTextContains(UserSeriesStatus.Completed.stringId.getResource())
        collection[2].assertTextContains(UserSeriesStatus.OnHold.stringId.getResource())
        collection[3].assertTextContains(UserSeriesStatus.Dropped.stringId.getResource())
        collection[4].assertTextContains(UserSeriesStatus.Planned.stringId.getResource())
    }

    /**
     * Checks if the "Current" option is checked.
     */
    fun currentIsSelected() {
        composeContentTestRule
            .onAllNodesWithTag(DialogTags.OptionRadio, true)
            .get(0)
            .assertIsSelected()
    }

    /**
     * Checks if the "Completed" option is checked.
     */
    fun completedIsSelected() {
        composeContentTestRule
            .onAllNodesWithTag(DialogTags.OptionRadio, true)
            .get(1)
            .assertIsSelected()
    }

    /**
     * Checks if the "On hold" option is checked.
     */
    fun onHoldIsSelected() {
        composeContentTestRule
            .onAllNodesWithTag(DialogTags.OptionRadio, true)
            .get(2)
            .assertIsSelected()
    }

    /**
     * Checks if the "Dropped" option is checked.
     */
    fun droppedIsSelected() {
        composeContentTestRule
            .onAllNodesWithTag(DialogTags.OptionRadio, true)
            .get(3)
            .assertIsSelected()
    }

    /**
     * Checks if the "Planned" option is checked.
     */
    fun plannedIsSelected() {
        composeContentTestRule
            .onAllNodesWithTag(DialogTags.OptionRadio, true)
            .get(4)
            .assertIsSelected()
    }
}
