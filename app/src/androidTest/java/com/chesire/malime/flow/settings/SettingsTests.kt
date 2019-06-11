package com.chesire.malime.flow.settings

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.chesire.malime.R
import com.chesire.malime.flow.Activity
import com.chesire.malime.helpers.injector
import com.chesire.malime.kitsu.AuthProvider
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotContains
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.rule.cleardata.ClearPreferencesRule
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class SettingsTests {
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
    fun canReachSettings() {
        activity.launchActivity(null)
        navigateToSettings()

        // If the version string is displayed, we are on the correct screen
        verifyOnSettings()
    }

    @Test
    fun acceptingLogoutExits() {
        activity.launchActivity(null)
        navigateToSettings()

        clickOn(R.string.settings_logout)
        clickOn(R.string.settings_logout_prompt_confirm)
        verifyNotOnSettings()
    }

    @Test
    fun decliningLogoutRemains() {
        activity.launchActivity(null)
        navigateToSettings()

        clickOn(R.string.settings_logout)
        clickOn(R.string.settings_logout_prompt_cancel)
        verifyOnSettings()
    }

    @Test
    @Ignore("Can't make this work without UIAutomator it looks like")
    fun privacyPolicyOpensNewPage() {
        activity.launchActivity(null)
        navigateToSettings()

        clickOn(R.string.settings_privacy_policy)
        verifyNotOnSettings()
    }

    @Test
    @Ignore("NYI")
    fun licensesOpensLicensesScreen() {
        // NYI
    }

    private fun navigateToSettings() {
        clickOn(R.id.profileFragment)
        clickOn(R.id.menuProfileSettings)
    }

    // If the version string is displayed, we are on settings
    private fun verifyOnSettings() = assertContains(R.string.version)

    // If the version string is no longer displayed, we are not settings
    private fun verifyNotOnSettings() = assertNotContains(R.string.version)
}
