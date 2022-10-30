package com.chesire.nekome.robots.settings

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.chesire.nekome.R

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
    fun openGitHub() {
        onView(withId(androidx.preference.R.id.recycler_view))
            .perform(
                actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText(R.string.settings_github)),
                    click()
                )
            )
    }

    /**
     * Open the licenses screen.
     */
    fun goToLicenses() {
        onView(withId(androidx.preference.R.id.recycler_view))
            .perform(
                actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText(R.string.settings_licenses)),
                    click()
                )
            )
    }

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
