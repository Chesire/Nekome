package com.chesire.nekome.features.series

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.R
import com.chesire.nekome.helpers.launchActivity
import com.chesire.nekome.helpers.login
import com.chesire.nekome.injection.DatabaseModule
import com.chesire.nekome.kitsu.AuthProvider
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotExist
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
@RunWith(AndroidJUnit4::class)
class SortTests {
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
    fun sortDialogDisplaysWithAllOptions() {
        launchActivity()
        clickMenu(R.id.menuSort)

        assertDisplayedAtPosition(R.id.md_recyclerview_content, 0, R.string.sort_by_default)
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 1, R.string.sort_by_title)
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 2, R.string.sort_by_start_date)
        assertDisplayedAtPosition(R.id.md_recyclerview_content, 3, R.string.sort_by_end_date)
    }

    @Test
    fun sortDialogClosesOnOptionSelected() {
        launchActivity()
        clickMenu(R.id.menuSort)

        assertDisplayed(R.string.sort_dialog_title)
        clickOn(R.string.sort_by_title)

        assertNotExist(R.string.sort_dialog_title)
    }
}
