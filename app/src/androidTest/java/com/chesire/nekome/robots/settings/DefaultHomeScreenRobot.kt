package com.chesire.nekome.robots.settings

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import com.chesire.nekome.core.compose.composables.DialogTags
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
import com.chesire.nekome.helpers.getResource
import com.chesire.nekome.robots.DialogResultsRobot
import com.chesire.nekome.robots.DialogRobot

/**
 * Robot to interact with the default home screen dialog.
 */
class DefaultHomeScreenRobot(
    private val composeContentTestRule: ComposeContentTestRule
) : DialogRobot(composeContentTestRule) {

    /**
     * Picks the "Anime" option.
     */
    fun chooseAnime() {
        composeContentTestRule
            .onAllNodesWithText(HomeScreenOptions.Anime.stringId.getResource())
            .filterToOne(hasParent(hasTestTag(DialogTags.Root)))
            .performClick()
    }

    /**
     * Picks the "Manga" option.
     */
    fun chooseManga() {
        composeContentTestRule
            .onAllNodesWithText(HomeScreenOptions.Manga.stringId.getResource())
            .filterToOne(hasParent(hasTestTag(DialogTags.Root)))
            .performClick()
    }

    /**
     * Executes validation steps.
     * Requires opening the dialog, performing the check, then closing the dialog again.
     */
    infix fun validate(func: DefaultHomeScreenResultRobot.() -> Unit) =
        DefaultHomeScreenResultRobot(composeContentTestRule).apply(func)
}

/**
 * Robot to check the results for the default home screen dialog.
 */
class DefaultHomeScreenResultRobot(
    private val composeContentTestRule: ComposeContentTestRule
) : DialogResultsRobot(composeContentTestRule) {

    /**
     * Assert that the options are in the correct locations.
     */
    fun isLoadedCorrectly() {
        val collection = composeContentTestRule.onAllNodesWithTag(DialogTags.OptionText, true)
        collection[0].assertTextContains(HomeScreenOptions.Anime.stringId.getResource())
        collection[1].assertTextContains(HomeScreenOptions.Manga.stringId.getResource())
    }

    /**
     * Checks if the "Anime" option is checked.
     */
    fun animeIsSelected() {
        composeContentTestRule
            .onAllNodesWithTag(DialogTags.OptionRadio, true)
            .get(0)
            .assertIsSelected()
    }

    /**
     * Checks if the "Manga" option is checked.
     */
    fun mangaIsSelected() {
        composeContentTestRule
            .onAllNodesWithTag(DialogTags.OptionRadio, true)
            .get(1)
            .assertIsSelected()
    }
}
