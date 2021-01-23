package com.chesire.nekome.robots.search

import com.chesire.nekome.R
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
    infix fun validate(func: SearchResultRobot.() -> Unit): SearchResultRobot {
        return SearchResultRobot().apply { func() }
    }
}

/**
 * Robot to check the results for the search screen.
 */
class SearchResultRobot {

    /**
     * Asserts the search screen is shown.
     */
    fun isVisible() = assertDisplayed(R.id.searchLayout)
}
