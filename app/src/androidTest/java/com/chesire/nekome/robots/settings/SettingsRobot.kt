package com.chesire.nekome.robots.settings

import com.chesire.nekome.R
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn

fun settings(func: SettingsRobot.() -> Unit) = SettingsRobot().apply { func() }

class SettingsRobot {

    /**
     * Options for selecting the default series state.
     */
    fun defaultSeriesState(func: DefaultSeriesStateRobot.() -> Unit) =
        DefaultSeriesStateRobot().apply { func() }

    fun changeDefaultHomeScreen() = clickOn(R.string.settings_default_home_title)

    fun changeTheme() = clickOn(R.string.settings_theme)

    fun clickGithub() = clickOn(R.string.settings_github)

    fun clickLicenses() = clickOn(R.string.settings_licenses)


    /**
     * Executes validation steps.
     */
    infix fun validate(func: SettingsResultRobot.() -> Unit): SettingsResultRobot {
        return SettingsResultRobot().apply { func() }
    }
}

class SettingsResultRobot {

    fun isVisible() {
        assertDisplayed(R.string.settings_default_series_status_title)
    }
}
