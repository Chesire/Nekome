package com.chesire.nekome.flow.series

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.chesire.nekome.R
import com.chesire.nekome.flow.Activity
import com.chesire.nekome.helpers.injector
import com.chesire.nekome.helpers.login
import com.chesire.nekome.kitsu.AuthProvider
import com.schibsted.spain.barista.assertion.BaristaEnabledAssertions.assertDisabled
import com.schibsted.spain.barista.assertion.BaristaEnabledAssertions.assertEnabled
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertCustomAssertionAtPosition
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu
import com.schibsted.spain.barista.rule.cleardata.ClearPreferencesRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class FilterTests {
    /*
    Since the MaterialDialogs library is being used, the way to access parts of the dialog needs to
    be slightly different, since the library uses a bunch of custom views, such as a control of a
    check box + a text view inside each item of a recycler view, as such all of the tests need to
    interact with `R.id.md_recyclerview_content` and the other `R.id.md_*` items.

    These tests are not exactly robust, since they assume initial state and rely on knowing the ids
    from the MaterialDialogs library, but they should do for now.
     */
    val activity = ActivityTestRule(Activity::class.java, false, false)

    @get:Rule
    val clearPreferencesRule = ClearPreferencesRule()

    @Inject
    lateinit var authProvider: AuthProvider

    @Before
    fun setUp() {
        injector.inject(this)
        authProvider.login()
    }

    @Test
    fun filterDialogDisplaysWithAllOptions() {
        activity.launchActivity(null)
        clickMenu(R.id.menuSeriesListFilter)

        assertDisplayedAtPosition(R.id.md_recyclerview_content, 0, R.string.filter_by_current)
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 1, R.string.filter_by_completed)
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 2, R.string.filter_by_on_hold)
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 3, R.string.filter_by_dropped)
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 4, R.string.filter_by_planned)
    }

    @Test
    fun filterDialogHasCorrectDefaultChoices() {
        activity.launchActivity(null)
        clickMenu(R.id.menuSeriesListFilter)

        assertPositionState(0, true)
        assertPositionState(1, false)
        assertPositionState(2, false)
        assertPositionState(3, false)
        assertPositionState(4, false)
    }

    @Test
    fun filterDialogRetainsLastChoices() {
        activity.launchActivity(null)
        clickMenu(R.id.menuSeriesListFilter)

        clickListItem(R.id.md_recyclerview_content, 2)
        clickListItem(R.id.md_recyclerview_content, 4)
        clickOn(R.id.md_button_positive)
        clickMenu(R.id.menuSeriesListFilter)

        assertPositionState(0, true)
        assertPositionState(1, false)
        assertPositionState(2, true)
        assertPositionState(3, false)
        assertPositionState(4, true)
    }

    @Test
    fun filterDialogCancelsOnCancelHit() {
        activity.launchActivity(null)
        clickMenu(R.id.menuSeriesListFilter)

        clickListItem(R.id.md_recyclerview_content, 2)
        clickListItem(R.id.md_recyclerview_content, 4)
        clickOn(R.id.md_button_negative)
        clickMenu(R.id.menuSeriesListFilter)

        assertPositionState(0, true)
        assertPositionState(1, false)
        assertPositionState(2, false)
        assertPositionState(3, false)
        assertPositionState(4, false)
    }

    @Test
    fun filterDialogConfirmDisabledIfNoFilter() {
        activity.launchActivity(null)
        clickMenu(R.id.menuSeriesListFilter)

        assertEnabled(R.id.md_button_positive)
        clickListItem(R.id.md_recyclerview_content, 0)

        assertDisabled(R.id.md_button_positive)
    }

    private fun assertPositionState(position: Int, expectedChecked: Boolean) {
        assertCustomAssertionAtPosition(
            R.id.md_recyclerview_content,
            position,
            R.id.md_control,
            matches(if (expectedChecked) isChecked() else isNotChecked())
        )
    }
}
