package com.chesire.nekome.robots.series

import com.chesire.nekome.R
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotExist
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu

/**
 * Robot to interact with the sort dialog.
 */
class SortOptionRobot {

    /**
     * Opens the sort dialog.
     */
    fun open() = clickMenu(R.id.menuSort)

    /**
     * Pick the "Default" option, requires first calling [open].
     */
    fun selectByDefault() = clickOn(R.string.sort_by_default)

    /**
     * Pick the "Title" option, requires first calling [open].
     */
    fun selectByTitle() = clickOn(R.string.sort_by_title)

    /**
     * Pick the "Start date" option, requires first calling [open].
     */
    fun selectByStartDate() = clickOn(R.string.sort_by_start_date)

    /**
     * Pick the "End date" option, requires first calling [open].
     */
    fun selectByEndDate() = clickOn(R.string.sort_by_end_date)

    /**
     * Executes validation steps.
     * Requires opening the dialog, performing the check.
     */
    infix fun validate(func: SortOptionResultsRobot.() -> Unit): SortOptionResultsRobot {
        return SortOptionResultsRobot().apply { func() }
    }
}

/**
 * Robot to check the results for the sort dialog.
 */
class SortOptionResultsRobot {

    /**
     * Assert that the sort options dialog is visible.
     */
    fun isVisible() = assertDisplayed(R.string.sort_dialog_title)

    /**
     * Assert that the sort options dialog is not visible.
     */
    fun isNotVisible() = assertNotExist(R.string.sort_dialog_title)

    /**
     * Assert that the sort options are in the correct locations.
     */
    fun isLoadedCorrectly() {
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 0, R.string.sort_by_default)
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 1, R.string.sort_by_title)
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 2, R.string.sort_by_start_date)
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 3, R.string.sort_by_end_date)
    }
}
