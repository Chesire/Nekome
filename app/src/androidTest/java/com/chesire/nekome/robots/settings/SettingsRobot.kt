package com.chesire.nekome.robots.settings

import com.chesire.nekome.R
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn

/**
 * Method to interact with the [SettingsRobot].
 */
fun settings(func: SettingsRobot.() -> Unit) = SettingsRobot().apply { func() }

/**
 * Robot to interact with the settings screen.
 */
class SettingsRobot {

    /**
     * Options for selecting the default series state.
     */
    fun defaultSeriesState(func: DefaultSeriesStateRobot.() -> Unit) =
        DefaultSeriesStateRobot().apply { func() }

    /**
     * Options for selecting the default home screen.
     */
    fun defaultHomeScreen(func: DefaultHomeScreenRobot.() -> Unit) =
        DefaultHomeScreenRobot().apply { func() }

    /**
     * Options for selecting the theme.
     */
    fun changeTheme(func: ThemeRobot.() -> Unit) = ThemeRobot().apply { func() }

    /**
     * Toggles the rate on completion setting.
     */
    fun changeRateOnComplete() = clickOn(R.string.settings_rate_on_completion_summary)

    /**
     * Clicks the link to go to the GitHub page.
     */
    fun openGitHub() = clickOn(R.string.settings_github)

    /**
     * Open the licenses screen.
     */
    fun goToLicenses() = clickOn(R.string.settings_licenses)

    /**
     * Executes validation steps.
     */
    infix fun validate(func: SettingsResultRobot.() -> Unit) =
        SettingsResultRobot().apply { func() }
}

/**
 * Robot to check the results for the settings screen.
 */
class SettingsResultRobot {

    /**
     * Asserts the settings screen is shown.
     */
    fun isVisible() = assertDisplayed(R.string.settings_default_series_status_title)
}
