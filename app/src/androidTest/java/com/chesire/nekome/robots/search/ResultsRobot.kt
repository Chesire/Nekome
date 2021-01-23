package com.chesire.nekome.robots.search

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
    infix fun validate(func: ResultsResultRobot.() -> Unit): ResultsResultRobot {
        return ResultsResultRobot().apply { func() }
    }
}

class ResultsResultRobot {

    /**
     * Asserts the search screen is shown.
     */
    fun isVisible() = assertDisplayed(R.id.resultsLayout)
}
