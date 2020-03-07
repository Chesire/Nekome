package com.chesire.nekome.flow.series

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.chesire.nekome.Activity
import com.chesire.nekome.R
import com.chesire.nekome.helpers.injector
import com.chesire.nekome.helpers.login
import com.chesire.nekome.kitsu.AuthProvider
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotExist
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class SortTests {
    val activity = ActivityTestRule(Activity::class.java, false, false)

    @Inject
    lateinit var authProvider: AuthProvider

    @Before
    fun setUp() {
        injector.inject(this)
        authProvider.login()
    }

    @Test
    fun sortDialogDisplaysWithAllOptions() {
        activity.launchActivity(null)
        clickMenu(R.id.menuSeriesListSort)

        assertDisplayedAtPosition(R.id.md_recyclerview_content, 0, R.string.sort_by_default)
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 1, R.string.sort_by_title)
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 2, R.string.sort_by_start_date)
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 3, R.string.sort_by_end_date)
    }

    @Test
    fun sortDialogClosesOnOptionSelected() {
        activity.launchActivity(null)
        clickMenu(R.id.menuSeriesListSort)

        assertDisplayed(R.string.sort_dialog_title)
        clickOn(R.string.sort_by_title)

        assertNotExist(R.string.sort_dialog_title)
    }
}
