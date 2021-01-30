package com.chesire.nekome.robots.login

import com.chesire.nekome.R
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed

/**
 * Method to interact with the [LoginSyncingRobot].
 */
fun loginSyncing(func: LoginSyncingRobot.() -> Unit) = LoginSyncingRobot().apply { func() }

/**
 * Robot to interact with the login syncing screen.
 */
class LoginSyncingRobot {

    /**
     * Executes validation steps.
     */
    infix fun validate(func: LoginSyncingResultRobot.() -> Unit) =
        LoginSyncingResultRobot().apply { func() }
}

/**
 * Robot to check the results for the login syncing screen.
 */
class LoginSyncingResultRobot {

    /**
     * Asserts the login syncing screen is shown.
     */
    fun isVisible() {
        assertDisplayed(R.id.syncingLayout)
    }
}
