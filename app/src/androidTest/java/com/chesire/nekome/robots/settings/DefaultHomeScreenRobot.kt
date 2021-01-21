package com.chesire.nekome.robots.settings

import com.chesire.nekome.R
import com.chesire.nekome.core.flags.HomeScreenOptions
import com.schibsted.spain.barista.assertion.BaristaCheckedAssertions.assertChecked
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn

/**
 * Robot to interact with the default home screen dialog.
 */
class DefaultHomeScreenRobot {

    /**
     * Open the default home screen dialog, and picks the "Anime" option.
     */
    fun chooseAnime() {
        openDialog()
        clickOn(HomeScreenOptions.Anime.stringId)
    }

    /**
     * Open the default home screen dialog, and picks the "Manga" option.
     */
    fun chooseManga() {
        openDialog()
        clickOn(HomeScreenOptions.Manga.stringId)
    }

    private fun openDialog() = clickOn(R.string.settings_default_home_title)

    private fun closeDialog() = clickOn(android.R.string.cancel)

    /**
     * Executes validation steps.
     * Requires opening the dialog, performing the check, then closing the dialog again.
     */
    infix fun validate(func: DefaultHomeScreenResultRobot.() -> Unit): DefaultHomeScreenResultRobot {
        return DefaultHomeScreenResultRobot().apply {
            openDialog()
            func()
            closeDialog()
        }
    }
}

/**
 * Robot to check the results for the default home screen dialog.
 */
class DefaultHomeScreenResultRobot {

    /**
     * Checks if the "Anime" option is checked.
     */
    fun animeIsSelected() {
        assertChecked(HomeScreenOptions.Anime.stringId);
    }

    /**
     * Checks if the "Manga" option is checked.
     */
    fun mangaIsSelected() {
        assertChecked(HomeScreenOptions.Manga.stringId);
    }
}
