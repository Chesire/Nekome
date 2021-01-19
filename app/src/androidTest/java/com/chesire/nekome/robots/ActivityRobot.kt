package com.chesire.nekome.robots

import com.chesire.nekome.R
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaDrawerInteractions.openDrawer

/**
 * Method to interact with the [ActivityRobot].
 */
fun activity(func: ActivityRobot.() -> Unit) = ActivityRobot().apply { func() }

/**
 * Robot to interact with the activity screen.
 */
class ActivityRobot {

    fun goToAnime() {
        openDrawer()
        clickOn(R.string.nav_anime)
    }

    fun goToManga() {
        openDrawer()
        clickOn(R.string.nav_manga)
    }

    fun goToSearch() {
        openDrawer()
        clickOn(R.string.nav_search)
    }

    fun goToSettings() {
        openDrawer()
        clickOn(R.string.nav_settings)
    }

    infix fun logout(func: ActivityLogoutChoices.() -> Unit): ActivityLogoutChoices {
        openDrawer()
        clickOn(R.string.menu_logout)
        return ActivityLogoutChoices().apply { func() }
    }

    /**
     * Executes validation steps.
     */
    infix fun validate(func: ActivityResultRobot.() -> Unit): ActivityResultRobot {
        return ActivityResultRobot().apply { func() }
    }
}

class ActivityLogoutChoices {

    fun confirm() {
        clickOn(R.string.menu_logout_prompt_confirm)
    }

    fun cancel() {
        clickOn(R.string.menu_logout_prompt_cancel)
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
