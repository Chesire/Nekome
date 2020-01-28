package com.chesire.nekome.flow.series

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.chesire.nekome.R
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.flow.Activity
import com.chesire.nekome.helpers.injector
import com.chesire.nekome.helpers.login
import com.chesire.nekome.kitsu.AuthProvider
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class FilterTests {
    val activity = ActivityTestRule(Activity::class.java, false, false)

    @Inject
    lateinit var authProvider: AuthProvider
    @Inject
    lateinit var seriesDao: SeriesDao

    @Before
    fun setUp() {
        injector.inject(this)
        authProvider.login()
    }

    @Test
    fun filterDialogDisplaysWithAllOptions() {
        activity.launchActivity(null)
        clickMenu(R.id.menuSeriesListFilter)

        assertDisplayed(R.string.filter_by_completed)
        assertDisplayed(R.string.filter_by_current)
        assertDisplayed(R.string.filter_by_dropped)
        assertDisplayed(R.string.filter_by_on_hold)
        assertDisplayed(R.string.filter_by_planned)
    }
}
