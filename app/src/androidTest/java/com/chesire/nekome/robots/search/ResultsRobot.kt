package com.chesire.nekome.robots.search

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.chesire.nekome.R
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed

/**
 * Method to interact with the [ResultsRobot].
 */
fun results(func: ResultsRobot.() -> Unit) = ResultsRobot().apply { func() }

/**
 * Robot to interact with the results screen.
 */
class ResultsRobot {
    /**
     * Executes validation steps.
     */
    infix fun validate(func: ResultsResultRobot.() -> Unit) = ResultsResultRobot().apply { func() }
}

/**
 * Robot to check the results for the results screen.
 */
class ResultsResultRobot {

    /**
     * Asserts the search screen is shown.
     */
    fun isVisible() = assertDisplayed(R.id.resultsLayout)

    /**
     * Verifies the title of the results page is [value].
     */
    fun titleIs(value: String){
        onView(withId(R.id.appBarToolbar))
            .check(matches(hasDescendant(withText(value))));

    }
}
