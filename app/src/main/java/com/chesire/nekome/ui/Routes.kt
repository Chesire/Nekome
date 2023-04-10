package com.chesire.nekome.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.chesire.nekome.app.login.credentials.ui.CredentialsScreen
import com.chesire.nekome.app.login.syncing.ui.SyncingScreen
import com.chesire.nekome.app.search.host.ui.HostScreen
import com.chesire.nekome.app.series.collection.ui.CollectionScreen
import com.chesire.nekome.app.series.item.ui.ItemScreen
import com.chesire.nekome.app.settings.config.ui.ConfigScreen
import com.chesire.nekome.app.settings.oss.ui.OssScreen

/**
 * List of routes which can be accessed via the bottom navigation.
 */
val bottomNavRoutes = listOf<BottomNavTarget>(
    Screen.Anime,
    Screen.Manga,
    Screen.Host,
    Screen.Config
)

fun NavGraphBuilder.addLoginRoutes(
    navController: NavHostController,
    finishDestination: String
) {
    composable(Screen.Credentials.route) {
        CredentialsScreen { navController.navigate(Screen.Syncing.route) }
    }

    composable(Screen.Syncing.route) {
        SyncingScreen {
            navController.navigate(finishDestination) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    }
}

fun NavGraphBuilder.addSeriesRoutes(navController: NavHostController) {
    composable(
        route = Screen.Anime.route,
        arguments = Screen.Anime.args
    ) {
        CollectionScreen { seriesId, seriesTitle ->
            navController.navigate("${Screen.Item.destination}/$seriesId/$seriesTitle")
        }
    }

    composable(
        route = Screen.Manga.route,
        arguments = Screen.Manga.args
    ) {
        CollectionScreen { seriesId, seriesTitle ->
            navController.navigate("${Screen.Item.destination}/$seriesId/$seriesTitle")
        }
    }

    composable(
        route = Screen.Item.route,
        arguments = Screen.Item.args
    ) {
        ItemScreen { navController.popBackStack() }
    }
}

fun NavGraphBuilder.addSearchRoutes() {
    composable(Screen.Host.route) {
        HostScreen()
    }
}

fun NavGraphBuilder.addSettingsRoutes(navController: NavHostController) {
    composable(Screen.Config.route) {
        ConfigScreen(
            navigateToOssScreen = { navController.navigate(Screen.OSS.route) },
            navigateAfterLogout = {
                navController.navigate(Screen.Credentials.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        )
    }

    composable(Screen.OSS.route) {
        OssScreen()
    }
}
