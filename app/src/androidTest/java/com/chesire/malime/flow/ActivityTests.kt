package com.chesire.malime.flow

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.chesire.malime.R
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
class ActivityTests {
    @get:Rule
    val activity = ActivityTestRule(Activity::class.java, false, false)
    @get:Rule
    val clearPreferencesRule = ClearPreferencesRule()

    @Inject
    lateinit var authProvider: AuthProvider

    @Before
    fun setUp() {
        injector.inject(this)
        authProvider.accessToken = "DummyToken"
    }

    @Test
    fun overviewStartsInAnimeView() {
        activity.launchActivity(null)
        assertDisplayed(R.string.nav_anime)
    }

    @Test
    fun overviewCanNavigateToAnimeView() {
        activity.launchActivity(null)
        assertDisplayed(R.string.nav_anime)
        clickOn(R.id.mangaFragment)
        assertDisplayed(R.string.nav_manga)
        clickOn(R.id.animeFragment)
        assertDisplayed(R.string.nav_anime)
    }

    @Test
    fun overviewCanNavigateToMangaView() {
        activity.launchActivity(null)
        clickOn(R.id.mangaFragment)
        assertDisplayed(R.string.nav_manga)
    }

    @Test
    fun overviewCanNavigateToProfileView() {
        activity.launchActivity(null)
        clickOn(R.id.profileFragment)
        assertDisplayed(R.string.nav_profile)
    }

    @Test
    fun overviewCanNavigateToActivityView() {
        activity.launchActivity(null)
        clickOn(R.id.timelineFragment)
        assertDisplayed(R.string.nav_timeline)
    }
}
