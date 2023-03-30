package com.chesire.nekome.robots

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.chesire.nekome.core.compose.composables.DialogTags

abstract class DialogRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Confirms the dialog option.
     */
    fun confirm() {
        composeContentTestRule
            .onNodeWithTag(DialogTags.OkButton)
            .performClick()
    }

    /**
     * Cancels the dialog option.
     */
    fun cancel() {
        composeContentTestRule
            .onNodeWithTag(DialogTags.CancelButton)
            .performClick()
    }
}

abstract class DialogResultsRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Assert that the dialog is visible.
     */
    fun isVisible() {
        composeContentTestRule
            .onNodeWithTag(DialogTags.Root)
            .assertIsDisplayed()
    }

    /**
     * Assert that the dialog is not visible.
     */
    fun isNotVisible() {
        try {
            composeContentTestRule
                .onNodeWithTag(DialogTags.Root)
                .assertIsNotDisplayed()
        } catch (ex: AssertionError) {
            // If an ex is thrown, then the node wasn't available
        }
    }
}
