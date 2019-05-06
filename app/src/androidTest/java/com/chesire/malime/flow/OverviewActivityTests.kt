package com.chesire.malime.flow

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.chesire.malime.R
import com.chesire.malime.TestApplication
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.rule.BaristaRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OverviewActivityTests {
    /*
        @Inject
        lateinit var authApi: AuthApi

        val auth = authApi
        coEvery { auth.login("", "") } throws NullPointerException()
        overviewActivity.launchActivity()
     */

    @get:Rule
    val overviewActivity = BaristaRule.create(OverviewActivity::class.java)

    @Before
    fun setUp() {
        val app = getInstrumentation().targetContext.applicationContext as TestApplication
        app.component.inject(this)
    }

    @Test
    fun overviewStartsInAnimeView() {
        overviewActivity.launchActivity()
        assertDisplayed(R.string.nav_anime)
    }

    @Test
    fun overviewCanNavigateToAnimeView() {
        overviewActivity.launchActivity()
        assertDisplayed(R.string.nav_anime)
        clickOn(R.id.mangaFragment)
        assertDisplayed(R.string.nav_manga)
        clickOn(R.id.animeFragment)
        assertDisplayed(R.string.nav_anime)
    }

    @Test
    fun overviewCanNavigateToMangaView() {
        overviewActivity.launchActivity()
        clickOn(R.id.mangaFragment)
        assertDisplayed(R.string.nav_manga)
    }

    @Test
    fun overviewCanNavigateToProfileView() {
        overviewActivity.launchActivity()
        clickOn(R.id.profileFragment)
        assertDisplayed(R.string.nav_profile)
    }

    @Test
    fun overviewCanNavigateToActivityView() {
        overviewActivity.launchActivity()
        clickOn(R.id.timelineFragment)
        assertDisplayed(R.string.nav_timeline)
    }

    @Test
    fun overviewCanNavigateToSettingsView() {
        overviewActivity.launchActivity()
        clickOn(R.id.settingsFragment)
        assertDisplayed(R.string.nav_settings)
    }
}
