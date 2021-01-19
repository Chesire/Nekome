package com.chesire.nekome.features.settings

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.R
import com.chesire.nekome.helpers.launchActivity
import com.chesire.nekome.helpers.login
import com.chesire.nekome.injection.DatabaseModule
import com.chesire.nekome.kitsu.AuthProvider
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotContains
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaDrawerInteractions.openDrawer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
@RunWith(AndroidJUnit4::class)
class SettingsTests {
    @get:Rule
    val hilt = HiltAndroidRule(this)

    @Inject
    lateinit var authProvider: AuthProvider

    @Before
    fun setUp() {
        hilt.inject()
        authProvider.login()
    }

    @Test
    fun canReachSettings() {
        launchActivity()
        navigateToSettings()

        verifyOnSettings()
    }

    @Test
    @Ignore("Can't make this work without UIAutomator it looks like")
    fun privacyPolicyOpensNewPage() {
        launchActivity()
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
        openDrawer()
        clickOn(R.string.nav_settings)
    }

    // If the version string is displayed, we are on settings
    private fun verifyOnSettings() = assertContains(R.string.version)

    // If the version string is no longer displayed, we are not settings
    private fun verifyNotOnSettings() = assertNotContains(R.string.version)
}
