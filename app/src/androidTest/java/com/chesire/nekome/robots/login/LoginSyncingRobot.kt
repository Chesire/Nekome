package com.chesire.nekome.robots.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.chesire.nekome.app.login.syncing.ui.SyncingTags

/**
 * Method to interact with the [LoginSyncingRobot].
 */
fun loginSyncing(
    composeContentTestRule: ComposeContentTestRule,
    func: LoginSyncingRobot.() -> Unit
) = LoginSyncingRobot(composeContentTestRule).apply { func() }

/**
 * Robot to interact with the login syncing screen.
 */
class LoginSyncingRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Executes validation steps.
     */
    infix fun validate(func: LoginSyncingResultRobot.() -> Unit) =
        LoginSyncingResultRobot(composeContentTestRule).apply { func() }
}

/**
 * Robot to check the results for the login syncing screen.
 */
class LoginSyncingResultRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Asserts the login syncing screen is shown.
     */
    fun isVisible() {
        composeContentTestRule
            .onNodeWithTag(SyncingTags.Root)
            .assertIsDisplayed()
    }
}
