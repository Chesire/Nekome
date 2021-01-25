package com.chesire.nekome.features.series

import com.chesire.nekome.R
import com.chesire.nekome.UITest
import com.chesire.nekome.injection.DatabaseModule
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotExist
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
class SortTests : UITest() {

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
