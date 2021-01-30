package com.chesire.nekome.robots.search

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.chesire.nekome.R
import com.chesire.nekome.helpers.ToastMatcher.Companion.onToast
import com.schibsted.spain.barista.assertion.BaristaErrorAssertions.assertError
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo

/**
 * Method to interact with the [SearchRobot].
 */
fun search(func: SearchRobot.() -> Unit) = SearchRobot().apply { func() }

/**
 * Robot to interact with the search screen.
 */
class SearchRobot {

    /**
     * Enters the term to search for.
     */
    fun searchTerm(term: String) = writeTo(R.id.searchText, term)

    /**
     * Selects the anime chip.
     */
    fun selectAnime() = clickOn(R.id.searchChipAnime)

    /**
     * Selects the manga chip.
     */
    fun selectManga() = clickOn(R.id.searchChipManga)

    /**
     * Clicks the search button.
     */
    fun clickSearch() = clickOn(R.id.searchConfirmButton)

    /**
     * Executes validation steps.
     */
    infix fun validate(func: SearchResultRobot.() -> Unit) = SearchResultRobot().apply { func() }
}

/**
 * Robot to check the results for the search screen.
 */
class SearchResultRobot {

    /**
     * Asserts the search screen is shown.
     */
    fun isVisible() = assertDisplayed(R.id.searchLayout)

    /**
     * Asserts the error for having no search term.
     */
    fun isEmptySearchError() = assertError(R.id.searchTextLayout, R.string.search_error_no_text)

    /**
     * Asserts the generic error message.
     */
    fun isGenericError() {
        onToast(R.string.error_generic).check(matches(isDisplayed()))
    }

    /**
     * Asserts the error for no series found.
     */
    fun isNoSeriesFoundError() {
        onToast(R.string.search_error_no_series_found).check(matches(isDisplayed()))
    }
}
