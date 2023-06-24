package com.chesire.nekome.robots.settings

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.chesire.nekome.core.compose.composables.DialogTags
import com.chesire.nekome.core.preferences.flags.ImageQuality
import com.chesire.nekome.helpers.getResource
import com.chesire.nekome.robots.DialogResultsRobot
import com.chesire.nekome.robots.DialogRobot

class ImageQualityRobot(
    private val composeContentTestRule: ComposeContentTestRule
) : DialogRobot(composeContentTestRule) {

    /**
     * Picks the "Low" option.
     */
    fun chooseLow() {
        composeContentTestRule
            .onNodeWithText(ImageQuality.Low.stringId.getResource())
            .performClick()
    }

    /**
     * Picks the "Medium" option.
     */
    fun chooseMedium() {
        composeContentTestRule
            .onNodeWithText(ImageQuality.Medium.stringId.getResource())
            .performClick()
    }

    /**
     * Picks the "High" option.
     */
    fun chooseHigh() {
        composeContentTestRule
            .onNodeWithText(ImageQuality.High.stringId.getResource())
            .performClick()
    }

    /**
     * Executes validation steps.
     * Requires opening the dialog, performing the check, then closing the dialog again.
     */
    infix fun validate(func: ImageQualityResultRobot.() -> Unit) =
        ImageQualityResultRobot(composeContentTestRule).apply(func)
}

/**
 * Robot to check the results for the image quality dialog.
 */
class ImageQualityResultRobot(
    private val composeContentTestRule: ComposeContentTestRule
) : DialogResultsRobot(composeContentTestRule) {

    /**
     * Assert that the options are in the correct locations.
     */
    fun isLoadedCorrectly() {
        val collection = composeContentTestRule.onAllNodesWithTag(DialogTags.OptionText, true)
        collection[0].assertTextContains(ImageQuality.Low.stringId.getResource())
        collection[1].assertTextContains(ImageQuality.Medium.stringId.getResource())
        collection[2].assertTextContains(ImageQuality.High.stringId.getResource())
    }

    /**
     * Checks if the "Low" option is checked.
     */
    fun lowIsSelected() {
        composeContentTestRule
            .onAllNodesWithTag(DialogTags.OptionRadio, true)
            .get(0)
            .assertIsSelected()
    }

    /**
     * Checks if the "Medium" option is checked.
     */
    fun mediumIsSelected() {
        composeContentTestRule
            .onAllNodesWithTag(DialogTags.OptionRadio, true)
            .get(1)
            .assertIsSelected()
    }

    /**
     * Checks if the "High" option is checked.
     */
    fun highIsSelected() {
        composeContentTestRule
            .onAllNodesWithTag(DialogTags.OptionRadio, true)
            .get(2)
            .assertIsSelected()
    }
}
