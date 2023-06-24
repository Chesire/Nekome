package com.chesire.nekome.robots.settings

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.chesire.nekome.core.compose.composables.DialogTags
import com.chesire.nekome.core.preferences.flags.TitleLanguage
import com.chesire.nekome.helpers.getResource
import com.chesire.nekome.robots.DialogResultsRobot
import com.chesire.nekome.robots.DialogRobot

class TitleLanguageRobot(
    private val composeContentTestRule: ComposeContentTestRule
) : DialogRobot(composeContentTestRule) {

    /**
     * Picks the "Canonical" option.
     */
    fun chooseCanonical() {
        composeContentTestRule
            .onNodeWithText(TitleLanguage.Canonical.stringId.getResource())
            .performClick()
    }

    /**
     * Picks the "English" option.
     */
    fun chooseEnglish() {
        composeContentTestRule
            .onNodeWithText(TitleLanguage.English.stringId.getResource())
            .performClick()
    }

    /**
     * Picks the "Romaji" option.
     */
    fun chooseRomaji() {
        composeContentTestRule
            .onNodeWithText(TitleLanguage.Romaji.stringId.getResource())
            .performClick()
    }

    /**
     * Picks the "Japanese" option.
     */
    fun chooseJapanese() {
        composeContentTestRule
            .onNodeWithText(TitleLanguage.Japanese.stringId.getResource())
            .performClick()
    }

    /**
     * Executes validation steps.
     * Requires opening the dialog, performing the check, then closing the dialog again.
     */
    infix fun validate(func: TitleLanguageResultRobot.() -> Unit) =
        TitleLanguageResultRobot(composeContentTestRule).apply(func)
}

/**
 * Robot to check the results for the title language dialog.
 */
class TitleLanguageResultRobot(
    private val composeContentTestRule: ComposeContentTestRule
) : DialogResultsRobot(composeContentTestRule) {

    /**
     * Assert that the options are in the correct locations.
     */
    fun isLoadedCorrectly() {
        val collection = composeContentTestRule.onAllNodesWithTag(DialogTags.OptionText, true)
        collection[0].assertTextContains(TitleLanguage.Canonical.stringId.getResource())
        collection[1].assertTextContains(TitleLanguage.English.stringId.getResource())
        collection[2].assertTextContains(TitleLanguage.Romaji.stringId.getResource())
        collection[3].assertTextContains(TitleLanguage.Japanese.stringId.getResource())
    }

    /**
     * Checks if the "Canonical" option is checked.
     */
    fun canonicalIsSelected() {
        composeContentTestRule
            .onAllNodesWithTag(DialogTags.OptionRadio, true)
            .get(0)
            .assertIsSelected()
    }

    /**
     * Checks if the "English" option is checked.
     */
    fun englishIsSelected() {
        composeContentTestRule
            .onAllNodesWithTag(DialogTags.OptionRadio, true)
            .get(1)
            .assertIsSelected()
    }

    /**
     * Checks if the "Romaji" option is checked.
     */
    fun romajiIsSelected() {
        composeContentTestRule
            .onAllNodesWithTag(DialogTags.OptionRadio, true)
            .get(2)
            .assertIsSelected()
    }

    /**
     * Checks if the "Japanese" option is checked.
     */
    fun japaneseIsSelected() {
        composeContentTestRule
            .onAllNodesWithTag(DialogTags.OptionRadio, true)
            .get(3)
            .assertIsSelected()
    }
}
