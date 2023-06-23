package com.chesire.nekome.app.settings.config.ui

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
import com.chesire.nekome.core.preferences.flags.ImageQuality
import com.chesire.nekome.core.preferences.flags.Theme

data class UIState(
    val userModel: UserModel?,
    val showLogoutDialog: Boolean,
    val executeLogout: Unit?,
    val themeValue: Theme,
    val showThemeDialog: Boolean,
    val defaultHomeValue: HomeScreenOptions,
    val showDefaultHomeDialog: Boolean,
    val defaultSeriesStatusValue: UserSeriesStatus,
    val showDefaultSeriesStatusDialog: Boolean,
    val imageQualityValue: ImageQuality,
    val showImageQualityDialog: Boolean,
    val rateSeriesValue: Boolean
) {
    companion object {
        val default = UIState(
            userModel = null,
            showLogoutDialog = false,
            executeLogout = null,
            themeValue = Theme.System,
            showThemeDialog = false,
            defaultHomeValue = HomeScreenOptions.Anime,
            showDefaultHomeDialog = false,
            defaultSeriesStatusValue = UserSeriesStatus.Current,
            showDefaultSeriesStatusDialog = false,
            imageQualityValue = ImageQuality.Low,
            showImageQualityDialog = false,
            rateSeriesValue = false
        )
    }
}

data class UserModel(
    val avatarUrl: String,
    val userName: String
)
