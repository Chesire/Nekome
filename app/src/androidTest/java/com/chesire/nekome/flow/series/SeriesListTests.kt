package com.chesire.nekome.flow.series

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.chesire.nekome.R
import com.chesire.nekome.flow.Activity
import com.chesire.nekome.helpers.injector
import com.chesire.nekome.helpers.login
import com.chesire.nekome.kitsu.AuthProvider
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class SeriesListTests {
    @get:Rule
    val activity = ActivityTestRule(Activity::class.java, false, false)

    @Inject
    lateinit var authProvider: AuthProvider

    @Before
    fun setUp() {
        injector.inject(this)
        authProvider.login()
    }

    @Test
    fun canReachSeriesList() {
        activity.launchActivity(null)

        assertDisplayed(R.id.fragmentSeriesListLayout)
    }

    @Test
    fun emptyListDisplaysEmptyView() {
        activity.launchActivity(null)

        assertDisplayed(R.id.fragmentSeriesListEmpty)
    }
}
