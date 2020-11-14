package com.chesire.nekome.flow

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.Activity
import com.chesire.nekome.R
import com.chesire.nekome.kitsu.AuthProvider
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotExist
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaDrawerInteractions.openDrawer
import com.schibsted.spain.barista.rule.cleardata.ClearPreferencesRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ActivityTests {
    @get:Rule(order = 0)
    val hilt = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val clearPreferencesRule = ClearPreferencesRule()

    @Inject
    lateinit var authProvider: AuthProvider

    @Before
    fun setUp() {
        hilt.inject()
        authProvider.accessToken = "DummyToken"
    }

    @Test
    fun overviewStartsInAnimeView() {
        launch()
        assertDisplayed(R.string.nav_anime)
    }

    @Test
    fun overviewCanNavigateToAnimeView() {
        launch()
        assertDisplayed(R.string.nav_anime)
        openDrawer()
        clickOn(R.string.nav_manga)
        assertDisplayed(R.string.nav_manga)
        openDrawer()
        clickOn(R.string.nav_anime)
        assertDisplayed(R.string.nav_anime)
    }

    @Test
    fun overviewCanNavigateToMangaView() {
        launch()
        openDrawer()
        clickOn(R.string.nav_manga)
        assertDisplayed(R.string.nav_manga)
    }

    @Test
    fun overviewCanNavigateToSettingsView() {
        launch()
        openDrawer()
        clickOn(R.string.nav_settings)
        assertDisplayed(R.string.settings_version)
    }

    @Test
    fun acceptingLogoutExits() {
        launch()
        openDrawer()

        clickOn(R.string.menu_logout)
        clickOn(R.string.menu_logout_prompt_confirm)

        assertDisplayed(R.id.detailsLayout)
    }

    @Test
    fun decliningLogoutRemains() {
        launch()
        openDrawer()

        clickOn(R.string.menu_logout)
        clickOn(R.string.menu_logout_prompt_cancel)

        assertNotExist(R.id.detailsLayout)
    }

    private fun launch() = ActivityScenario.launch(Activity::class.java)
}
