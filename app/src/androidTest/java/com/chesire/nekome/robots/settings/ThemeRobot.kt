package com.chesire.nekome.robots.settings

import com.adevinta.android.barista.assertion.BaristaCheckedAssertions.assertChecked
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.chesire.nekome.R
import com.chesire.nekome.core.preferences.flags.Theme

/**
 * Robot to interact with the theme dialog.
 */
class ThemeRobot {

    /**
     * Open the theme dialog, and picks the "System default" option.
     */
    fun chooseSystem() {
        openDialog()
        clickOn(Theme.System.stringId)
    }

    /**
     * Open the theme dialog, and picks the "Light" option.
     */
    fun chooseLight() {
        openDialog()
        clickOn(Theme.Light.stringId)
    }

    /**
     * Open the theme dialog, and picks the "Dark" option.
     */
    fun chooseDark() {
        openDialog()
        clickOn(Theme.Dark.stringId)
    }

    private fun openDialog() = clickOn(R.string.settings_theme)

    private fun closeDialog() = clickOn(android.R.string.cancel)

    /**
     * Executes validation steps.
     * Requires opening the dialog, performing the check, then closing the dialog again.
     */
    infix fun validate(func: ThemeResultRobot.() -> Unit): ThemeResultRobot {
        return ThemeResultRobot().apply {
            openDialog()
            func()
            closeDialog()
        }
    }
}

/**
 * Robot to check the results for the theme dialog.
 */
class ThemeResultRobot {

    /**
     * Checks if the "System default" option is checked.
     */
    fun systemIsSelected() = assertChecked(Theme.System.stringId)

    /**
     * Checks if the "Light" option is checked.
     */
    fun lightIsSelected() = assertChecked(Theme.Light.stringId)

    /**
     * Checks if the "Dark" option is checked.
     */
    fun darkIsSelected() = assertChecked(Theme.Dark.stringId)
}
