package com.chesire.nekome.robots.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.chesire.nekome.app.search.search.ui.SearchTags

/**
 * Method to interact with the [SearchRobot].
 */
fun search(
    composeContentTestRule: ComposeContentTestRule,
    func: SearchRobot.() -> Unit
) = SearchRobot(composeContentTestRule).apply(func)

/**
 * Robot to interact with the search host screen.
 */
class SearchRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Enters the term to search for.
     */
    fun searchTerm(term: String) {
        composeContentTestRule
            .onNodeWithTag(SearchTags.Input)
            .performTextInput(term)
    }

    /**
     * Selects the anime chip.
     */
    fun selectAnime() {
        composeContentTestRule
            .onNodeWithTag(SearchTags.Anime)
            .performClick()
    }

    /**
     * Selects the manga chip.
     */
    fun selectManga() {
        composeContentTestRule
            .onNodeWithTag(SearchTags.Manga)
            .performClick()
    }

    /**
     * Clicks the search button.
     */
    fun clickSearch() {
        composeContentTestRule
            .onNodeWithTag(SearchTags.Search)
            .performClick()
    }

    /**
     * Executes validation steps.
     */
    infix fun validate(func: SearchResultRobot.() -> Unit) =
        SearchResultRobot(composeContentTestRule).apply(func)
}

/**
 * Robot to check the results for the search screen.
 */
class SearchResultRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Asserts the search screen is shown.
     */
    fun isVisible() {
        composeContentTestRule
            .onNodeWithTag(SearchTags.Root)
            .assertIsDisplayed()
    }

    /**
     * Asserts the error for having no search term.
     */
    fun isEmptySearchError() {
        composeContentTestRule
            .onNodeWithTag(SearchTags.Snackbar)
            .assertIsDisplayed()
            .onChild()
            .onChild()
            .onChild()
            .assertTextContains(
                value = "series title must be provided",
                ignoreCase = true,
                substring = true
            )
    }

    /**
     * Asserts the generic error message.
     */
    fun isGenericError() {
        composeContentTestRule
            .onNodeWithTag(SearchTags.Snackbar)
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
            .onNodeWithTag(SearchTags.Snackbar)
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
