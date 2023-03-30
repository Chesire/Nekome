package com.chesire.nekome.robots.settings

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.performClick
import com.chesire.nekome.core.compose.composables.DialogTags
import com.chesire.nekome.core.preferences.flags.Theme
import com.chesire.nekome.helpers.getResource
import com.chesire.nekome.robots.DialogResultsRobot
import com.chesire.nekome.robots.DialogRobot

/**
 * Robot to interact with the theme dialog.
 */
class ThemeRobot(
    private val composeContentTestRule: ComposeContentTestRule
) : DialogRobot(composeContentTestRule) {

    /**
     * Open the theme dialog, and picks the "System default" option.
     */
    fun chooseSystem() {
        composeContentTestRule
            .onAllNodesWithText(Theme.System.stringId.getResource())
            .onLast()
            .performClick()
    }

    /**
     * Open the theme dialog, and picks the "Dark" option.
     */
    fun chooseDark() {
        composeContentTestRule
            .onAllNodesWithText(Theme.Dark.stringId.getResource())
            .onLast()
            .performClick()
    }

    /**
     * Open the theme dialog, and picks the "Light" option.
     */
    fun chooseLight() {
        composeContentTestRule
            .onAllNodesWithText(Theme.Light.stringId.getResource())
            .onLast()
            .performClick()
    }

    /**
     * Executes validation steps.
     * Requires opening the dialog, performing the check, then closing the dialog again.
     */
    infix fun validate(func: ThemeResultRobot.() -> Unit) =
        ThemeResultRobot(composeContentTestRule).apply(func)
}

/**
 * Robot to check the results for the theme dialog.
 */
class ThemeResultRobot(
    private val composeContentTestRule: ComposeContentTestRule
) : DialogResultsRobot(composeContentTestRule) {

    /**
     * Assert that the options are in the correct locations.
     */
    fun isLoadedCorrectly() {
        val collection = composeContentTestRule.onAllNodesWithTag(DialogTags.OptionText, true)
        collection[0].assertTextContains(Theme.System.stringId.getResource())
        collection[1].assertTextContains(Theme.Dark.stringId.getResource())
        collection[2].assertTextContains(Theme.Light.stringId.getResource())
    }

    /**
     * Checks if the "System default" option is checked.
     */
    fun systemIsSelected() {
        composeContentTestRule
            .onAllNodesWithTag(DialogTags.OptionRadio, true)
            .get(0)
            .assertIsSelected()
    }

    /**
     * Checks if the "Dark" option is checked.
     */
    fun darkIsSelected() {
        composeContentTestRule
            .onAllNodesWithTag(DialogTags.OptionRadio, true)
            .get(1)
            .assertIsSelected()
    }

    /**
     * Checks if the "Light" option is checked.
     */
    fun lightIsSelected() {
        composeContentTestRule
            .onAllNodesWithTag(DialogTags.OptionRadio, true)
            .get(2)
            .assertIsSelected()
    }
}
