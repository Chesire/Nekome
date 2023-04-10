package com.chesire.nekome.robots

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.chesire.nekome.ui.MainActivityTags

/**
 * Method to interact with the [ActivityRobot].
 */
fun activity(
    composeContentTestRule: ComposeContentTestRule,
    func: ActivityRobot.() -> Unit
) = ActivityRobot(composeContentTestRule).apply(func)

/**
 * Robot to interact with the activity screen.
 */
class ActivityRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Executes navigation to the Anime screen.
     */
    fun goToAnime() {
        composeContentTestRule
            .onNodeWithTag(MainActivityTags.Anime)
            .performClick()
    }

    /**
     * Executes navigation to the Manga screen.
     */
    fun goToManga() {
        composeContentTestRule
            .onNodeWithTag(MainActivityTags.Manga)
            .performClick()
    }

    /**
     * Executes navigation to the Search screen.
     */
    fun goToSearch() {
        composeContentTestRule
            .onNodeWithTag(MainActivityTags.Search)
            .performClick()
    }

    /**
     * Executes navigation to the Settings screen.
     */
    fun goToSettings() {
        composeContentTestRule
            .onNodeWithTag(MainActivityTags.Settings)
            .performClick()
    }

    /**
     * Executes validation steps.
     */
    infix fun validate(func: ActivityResultRobot.() -> Unit) =
        ActivityResultRobot(composeContentTestRule).apply(func)
}

/**
 * Robot to check the results for the activity screen.
 */
class ActivityResultRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Asserts the main activity screen is shown.
     */
    fun isVisible() {
        composeContentTestRule
            .onNodeWithTag(MainActivityTags.Root)
            .assertIsDisplayed()
    }

    /**
     * Asserts that the anime screen is currently shown.
     */
    fun isOnAnimeScreen() {
        composeContentTestRule
            .onNodeWithTag(MainActivityTags.Anime)
            .assertIsSelected()
    }

    /**
     * Asserts that the manga screen is currently shown.
     */
    fun isOnMangaScreen() {
        composeContentTestRule
            .onNodeWithTag(MainActivityTags.Manga)
            .assertIsSelected()
    }

    /**
     * Asserts that the search screen is currently shown.
     */
    fun isOnSearchScreen() {
        composeContentTestRule
            .onNodeWithTag(MainActivityTags.Search)
            .assertIsSelected()
    }

    /**
     * Asserts that the settings screen is currently shown.
     */
    fun isOnSettingsScreen() {
        composeContentTestRule
            .onNodeWithTag(MainActivityTags.Settings)
            .assertIsSelected()
    }
}
