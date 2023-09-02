package com.chesire.nekome.robots.settings

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.chesire.nekome.app.settings.config.ui.ConfigTags
import com.chesire.nekome.helpers.getResource
import com.chesire.nekome.core.resources.R as Resource

/**
 * Method to interact with the [ConfigRobot].
 */
fun config(composeContentTestRule: ComposeContentTestRule, func: ConfigRobot.() -> Unit) =
    ConfigRobot(composeContentTestRule).apply(func)

/**
 * Robot to interact with the config screen.
 */
class ConfigRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Opens the default series state dialog.
     */
    fun clickDefaultSeriesState() {
        composeContentTestRule
            .onNodeWithText(Resource.string.settings_default_series_status_title.getResource())
            .performClick()
    }

    /**
     * Options for selecting the default series state.
     */
    fun defaultSeriesState(func: DefaultSeriesStateRobot.() -> Unit) =
        DefaultSeriesStateRobot(composeContentTestRule).apply(func)

    /**
     * Opens the default home screen dialog.
     */
    fun clickDefaultHomeScreen() {
        composeContentTestRule
            .onNodeWithText(Resource.string.settings_default_home_title.getResource())
            .performClick()
    }

    /**
     * Options for selecting the default home screen.
     */
    fun defaultHomeScreen(func: DefaultHomeScreenRobot.() -> Unit) =
        DefaultHomeScreenRobot(composeContentTestRule).apply(func)

    /**
     * Opens the image quality dialog.
     */
    fun clickImageQuality() {
        composeContentTestRule
            .onNodeWithText(Resource.string.settings_image_quality_title.getResource())
            .performClick()
    }

    /**
     * Options for selecting the image quality.
     */
    fun imageQuality(func: ImageQualityRobot.() -> Unit) =
        ImageQualityRobot(composeContentTestRule).apply(func)

    /**
     * Opens the title language dialog.
     */
    fun clickTitleLanguage() {
        composeContentTestRule
            .onNodeWithText(Resource.string.settings_title_language_title.getResource())
            .performClick()
    }

    /**
     * Options for selecting the title language.
     */
    fun titleLanguage(func: TitleLanguageRobot.() -> Unit) =
        TitleLanguageRobot(composeContentTestRule).apply(func)

    /**
     * Opens the theme dialog.
     */
    fun clickTheme() {
        composeContentTestRule
            .onNodeWithText(Resource.string.settings_theme.getResource())
            .performClick()
    }

    /**
     * Options for selecting the theme.
     */
    fun changeTheme(func: ThemeRobot.() -> Unit) = ThemeRobot(composeContentTestRule).apply(func)

    /**
     * Toggles the rate on completion setting.
     */
    fun changeRateOnComplete() {
        composeContentTestRule
            .onNodeWithText(Resource.string.settings_rate_on_completion_summary.getResource())
            .performClick()
    }

    /**
     * Open the licenses screen.
     */
    fun goToLicenses() {
        composeContentTestRule
            .onNodeWithText(Resource.string.settings_licenses.getResource())
            .performClick()
    }

    /**
     * Executes validation steps.
     */
    infix fun validate(func: ConfigResultRobot.() -> Unit) =
        ConfigResultRobot(composeContentTestRule).apply(func)
}

/**
 * Robot to check the results for the config screen.
 */
class ConfigResultRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Asserts the settings screen is shown.
     */
    fun isVisible() {
        composeContentTestRule
            .onNodeWithTag(ConfigTags.Root)
            .assertIsDisplayed()
    }
}
