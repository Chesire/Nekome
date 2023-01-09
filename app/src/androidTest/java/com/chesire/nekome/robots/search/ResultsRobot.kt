package com.chesire.nekome.robots.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.chesire.nekome.R
import com.chesire.nekome.app.search.results.ui.ResultsTags

/**
 * Method to interact with the [ResultsRobot].
 */
fun results(
    composeContentTestRule: ComposeContentTestRule,
    func: ResultsRobot.() -> Unit
) = ResultsRobot(composeContentTestRule).apply(func)

/**
 * Robot to interact with the results screen.
 */
class ResultsRobot(private val composeContentTestRule: ComposeContentTestRule) {
    /**
     * Executes validation steps.
     */
    infix fun validate(
        func: ResultsResultRobot.() -> Unit
    ) = ResultsResultRobot(composeContentTestRule).apply(func)
}

/**
 * Robot to check the results for the results screen.
 */
class ResultsResultRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Asserts the search screen is shown.
     */
    fun isVisible() {
        composeContentTestRule
            .onNodeWithTag(ResultsTags.Root)
            .assertIsDisplayed()
    }

    /**
     * Verifies the title of the results page is [value].
     */
    fun titleIs(value: String) {
        onView(withId(R.id.appBarToolbar))
            .check(matches(hasDescendant(withText(value))))
    }
}
