package com.chesire.nekome.robots.settings

import com.chesire.nekome.R
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.schibsted.spain.barista.assertion.BaristaCheckedAssertions.assertChecked
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn

/**
 * Robot to interact with the default series state dialog.
 */
class DefaultSeriesStateRobot {

    /**
     * Open the default series state dialog, and picks the "Current" option.
     */
    fun chooseCurrent() {
        openDialog()
        clickOn(UserSeriesStatus.Current.stringId)
    }

    /**
     * Open the default series state dialog, and picks the "Completed" option.
     */
    fun chooseCompleted() {
        openDialog()
        clickOn(UserSeriesStatus.Completed.stringId)
    }

    /**
     * Open the default series state dialog, and picks the "On hold" option.
     */
    fun chooseOnHold() {
        openDialog()
        clickOn(UserSeriesStatus.OnHold.stringId)
    }

    /**
     * Open the default series state dialog, and picks the "Dropped" option.
     */
    fun chooseDropped() {
        openDialog()
        clickOn(UserSeriesStatus.Dropped.stringId)
    }

    /**
     * Open the default series state dialog, and picks the "Planned" option.
     */
    fun choosePlanned() {
        openDialog()
        clickOn(UserSeriesStatus.Planned.stringId)
    }

    private fun openDialog() = clickOn(R.string.settings_default_series_status_title)

    private fun closeDialog() = clickOn(android.R.string.cancel)

    /**
     * Executes validation steps.
     * Requires opening the dialog, performing the check, then closing the dialog again.
     */
    infix fun validate(
        func: DefaultSeriesStateResultRobot.() -> Unit
    ): DefaultSeriesStateResultRobot {
        return DefaultSeriesStateResultRobot().apply {
            openDialog()
            func()
            closeDialog()
        }
    }
}

/**
 * Robot to check the results for the default series state dialog.
 */
class DefaultSeriesStateResultRobot {

    /**
     * Checks if the "Current" option is checked.
     */
    fun currentIsSelected() = assertChecked(UserSeriesStatus.Current.stringId)

    /**
     * Checks if the "Completed" option is checked.
     */
    fun completedIsSelected() = assertChecked(UserSeriesStatus.Completed.stringId)

    /**
     * Checks if the "On hold" option is checked.
     */
    fun onHoldIsSelected() = assertChecked(UserSeriesStatus.OnHold.stringId)

    /**
     * Checks if the "Dropped" option is checked.
     */
    fun droppedIsSelected() = assertChecked(UserSeriesStatus.Dropped.stringId)

    /**
     * Checks if the "Planned" option is checked.
     */
    fun plannedIsSelected() = assertChecked(UserSeriesStatus.Planned.stringId)
}
