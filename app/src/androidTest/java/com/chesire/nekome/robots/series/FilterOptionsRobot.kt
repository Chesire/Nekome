package com.chesire.nekome.robots.series

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import com.chesire.nekome.R
import com.schibsted.spain.barista.assertion.BaristaEnabledAssertions.assertDisabled
import com.schibsted.spain.barista.assertion.BaristaEnabledAssertions.assertEnabled
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertCustomAssertionAtPosition
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotExist
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu

/*
Since the MaterialDialogs library is being used, the way to access parts of the dialog needs to
be slightly different, since the library uses a bunch of custom views, such as a control of a
check box + a text view inside each item of a recycler view, as such all of the tests need to
interact with `R.id.md_recyclerview_content` and the other `R.id.md_*` items.
 */

/**
 * Robot to interact with the filter dialog.
 */
class FilterOptionsRobot {

    /**
     * Opens the filter dialog.
     */
    fun open() = clickMenu(R.id.menuFilter)

    /**
     * Pick the "Current" option, requires first calling [open].
     */
    fun clickCurrent() = clickListItem(R.id.md_recyclerview_content, 0)

    /**
     * Pick the "Completed" option, requires first calling [open].
     */
    fun clickCompleted() = clickListItem(R.id.md_recyclerview_content, 1)

    /**
     * Pick the "On hold" option, requires first calling [open].
     */
    fun clickOnHold() = clickListItem(R.id.md_recyclerview_content, 2)

    /**
     * Pick the "Dropped" option, requires first calling [open].
     */
    fun clickDropped() = clickListItem(R.id.md_recyclerview_content, 3)

    /**
     * Pick the "Planned" option, requires first calling [open].
     */
    fun clickPlanned() = clickListItem(R.id.md_recyclerview_content, 4)

    /**
     * Clicks the confirm dialog option, requires first calling [open].
     */
    fun confirm() = clickOn(R.id.md_button_positive)

    /**
     * Clicks the cancel dialog option, requires first calling [open].
     */
    fun cancel() = clickOn(R.id.md_button_negative)

    /**
     * Executes validation steps.
     * Requires opening the dialog, performing the check.
     */
    infix fun validate(func: FilterOptionsResultsRobot.() -> Unit): FilterOptionsResultsRobot {
        return FilterOptionsResultsRobot().apply { func() }
    }
}

/**
 * Robot to check the results for the filter dialog.
 */
class FilterOptionsResultsRobot {

    /**
     * Assert that the filter options dialog is visible.
     */
    fun isVisible() = assertDisplayed(R.string.filter_dialog_title)

    /**
     * Assert that the filter options dialog is not visible.
     */
    fun isNotVisible() = assertNotExist(R.string.filter_dialog_title)

    /**
     * Assert that the filter options are in the correct locations.
     */
    fun isLoadedCorrectly() {
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 0, R.string.filter_by_current)
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 1, R.string.filter_by_completed)
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 2, R.string.filter_by_on_hold)
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 3, R.string.filter_by_dropped)
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 4, R.string.filter_by_planned)
    }

    /**
     * Assert that the filter dialog options are in the default order.
     */
    fun isInDefaultState() {
        isCurrentChecked()
        isCompletedNotChecked()
        isOnHoldNotChecked()
        isDroppedNotChecked()
        isPlannedNotChecked()
    }

    /**
     * Assert that the confirm dialog option is enabled.
     */
    fun confirmIsEnabled() = assertEnabled(R.id.md_button_positive)

    /**
     * Assert that the confirm dialog option is disabled.
     */
    fun confirmIsDisabled() = assertDisabled(R.id.md_button_positive)

    /**
     * Assert that the "Current" choice is checked.
     */
    fun isCurrentChecked() = assertPositionState(0, true)

    /**
     * Assert that the "Current" choice is not checked.
     */
    fun isCurrentNotChecked() = assertPositionState(0, false)

    /**
     * Assert that the "Completed" choice is checked.
     */
    fun isCompletedChecked() = assertPositionState(1, true)

    /**
     * Assert that the "Completed" choice is not checked.
     */
    fun isCompletedNotChecked() = assertPositionState(1, false)

    /**
     * Assert that the "On hold" choice is checked.
     */
    fun isOnHoldChecked() = assertPositionState(2, true)

    /**
     * Assert that the "On hold" choice is not checked.
     */
    fun isOnHoldNotChecked() = assertPositionState(2, false)

    /**
     * Assert that the "Dropped" choice is checked.
     */
    fun isDroppedChecked() = assertPositionState(3, true)

    /**
     * Assert that the "Dropped" choice is not checked.
     */
    fun isDroppedNotChecked() = assertPositionState(3, false)

    /**
     * Assert that the "Planned" choice is checked.
     */
    fun isPlannedChecked() = assertPositionState(4, true)

    /**
     * Assert that the "Planned" choice is not checked.
     */
    fun isPlannedNotChecked() = assertPositionState(4, false)

    private fun assertPositionState(position: Int, expectedChecked: Boolean) {
        assertCustomAssertionAtPosition(
            R.id.md_recyclerview_content,
            position,
            R.id.md_control,
            matches(if (expectedChecked) isChecked() else isNotChecked())
        )
    }
}
