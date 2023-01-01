package com.chesire.nekome.robots.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.chesire.nekome.app.search.host.ui.HostTags

/**
 * Method to interact with the [HostRobot].
 */
fun host(
    composeContentTestRule: ComposeContentTestRule,
    func: HostRobot.() -> Unit
) = HostRobot(composeContentTestRule).apply(func)

/**
 * Robot to interact with the search host screen.
 */
class HostRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Enters the term to search for.
     */
    fun searchTerm(term: String) {
        composeContentTestRule
            .onNodeWithTag(HostTags.Input)
            .performTextInput(term)
    }

    /**
     * Selects the anime chip.
     */
    fun selectAnime() {
        composeContentTestRule
            .onNodeWithTag(HostTags.Anime)
            .performClick()
    }

    /**
     * Selects the manga chip.
     */
    fun selectManga() {
        composeContentTestRule
            .onNodeWithTag(HostTags.Manga)
            .performClick()
    }

    /**
     * Clicks the search button.
     */
    fun clickSearch() {
        composeContentTestRule
            .onNodeWithTag(HostTags.Search)
            .performClick()
    }

    /**
     * Executes validation steps.
     */
    infix fun validate(func: HostResultRobot.() -> Unit) =
        HostResultRobot(composeContentTestRule).apply(func)
}

/**
 * Robot to check the results for the search screen.
 */
class HostResultRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Asserts the search screen is shown.
     */
    fun isVisible() {
        composeContentTestRule
            .onNodeWithTag(HostTags.Root)
            .assertIsDisplayed()
    }

    /**
     * Asserts the error for having no search term.
     */
    fun isEmptySearchError() {
        // Need to add error message to the screen.
        // TODO: assertError(R.id.searchTextLayout, R.string.search_error_no_text)
    }

    /**
     * Asserts the generic error message.
     */
    fun isGenericError() {
        composeContentTestRule
            .onNodeWithTag(HostTags.Snackbar)
            .assertIsDisplayed()
            .onChild()
            .onChild()
            .onChild()
            .assertTextContains(
                value = "An unknown error has occurred",
                ignoreCase = true,
                substring = true
            )
    }

    /**
     * Asserts the error for no series found.
     */
    fun isNoSeriesFoundError() {
        composeContentTestRule
            .onNodeWithTag(HostTags.Snackbar)
            .assertIsDisplayed()
            .onChild()
            .onChild()
            .onChild()
            .assertTextContains(
                value = "No series could be found",
                ignoreCase = true,
                substring = true
            )
    }
}
