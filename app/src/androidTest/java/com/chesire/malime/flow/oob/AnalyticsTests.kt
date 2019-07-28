package com.chesire.malime.flow.oob

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.chesire.malime.R
import com.chesire.malime.SharedPref
import com.chesire.malime.flow.Activity
import com.chesire.malime.helpers.injector
import com.chesire.malime.kitsu.AuthProvider
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.rule.cleardata.ClearPreferencesRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class AnalyticsTests {
    @get:Rule
    val activity = ActivityTestRule(Activity::class.java, false, false)
    @get:Rule
    val clearPreferencesRule = ClearPreferencesRule()

    @Inject
    lateinit var sharedPref: SharedPref
    @Inject
    lateinit var authProvider: AuthProvider

    @Before
    fun setUp() {
        injector.inject(this)

        authProvider.accessToken = ""
        sharedPref.isAnalyticsComplete = false
    }

    @Test
    fun titleIsDisplayed() {
        activity.launchActivity(null)

        assertDisplayed(R.string.analytics_allow)
    }

    @Test
    fun denyButtonGoesToNextScreen() {
        activity.launchActivity(null)

        clickOn(R.id.fragmentAnalyticsDeny)
        assertDisplayed(R.id.fragmentDetailsLayout)
    }

    @Test
    fun confirmButtonGoesToNextScreen() {
        activity.launchActivity(null)

        clickOn(R.id.fragmentAnalyticsConfirm)
        assertDisplayed(R.id.fragmentDetailsLayout)
    }
}
