package com.chesire.nekome.robots

import com.chesire.nekome.R
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed

/**
 * Method to interact with the [ActivityRobot].
 */
fun activity(func: ActivityRobot.() -> Unit) = ActivityRobot().apply { func() }

/**
 * Robot to interact with the activity screen.
 */
class ActivityRobot {

    /**
     * Executes validation steps.
     */
    infix fun validate(func: ActivityResultRobot.() -> Unit): ActivityResultRobot {
        return ActivityResultRobot().apply { func() }
    }
}

/**
 * Robot to check the results for the activity screen.
 */
class ActivityResultRobot {

    /**
     * Asserts the login details screen is shown.
     */
    fun isVisible() {
        assertDisplayed(R.id.activityNavigation)
    }
}
