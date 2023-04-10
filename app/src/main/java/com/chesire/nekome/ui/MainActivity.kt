package com.chesire.nekome.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.app.login.credentials.ui.CredentialsScreen
import com.chesire.nekome.app.login.syncing.ui.SyncingScreen
import com.chesire.nekome.app.search.host.ui.HostScreen
import com.chesire.nekome.app.series.collection.ui.CollectionScreen
import com.chesire.nekome.app.series.item.ui.ItemScreen
import com.chesire.nekome.app.settings.config.ui.ConfigScreen
import com.chesire.nekome.app.settings.oss.ui.OssScreen
import com.chesire.nekome.core.compose.theme.NekomeTheme
import dagger.hilt.android.AndroidEntryPoint

@LogLifecykle
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()
    private val bottomNavItems = listOf<BottomNavTarget>(
        Screen.Anime,
        Screen.Manga,
        Screen.Host,
        Screen.Config
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NekomeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val state = viewModel.uiState.collectAsState()
                    val navController = rememberNavController()
                    val scaffoldState = rememberScaffoldState()

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        scaffoldState = scaffoldState,
                        bottomBar = {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            if (Screen.showsBottomNav(navBackStackEntry?.destination?.route)) {
                                BottomNavigation {
                                    val currentDestination = navBackStackEntry?.destination
                                    bottomNavItems.forEach { screen ->
                                        check(screen is Screen)
                                        BottomNavigationItem(
                                            icon = {
                                                Icon(
                                                    painter = painterResource(id = screen.icon),
                                                    contentDescription = stringResource(id = screen.title)
                                                )
                                            },
                                            label = { Text(stringResource(screen.title)) },
                                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                            onClick = {
                                                navController.navigate(screen.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = false
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        },
                    ) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = if (state.value.userLoggedIn) {
                                state.value.defaultHomeScreen
                            } else {
                                Screen.Credentials.route
                            },
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            addLoginRoutes(navController, state.value.defaultHomeScreen)
                            addSeriesRoutes(navController)
                            addSearchRoutes()
                            addSettingsRoutes(navController)
                        }
                    }
                }
            }
        }
    }
}

private fun NavGraphBuilder.addLoginRoutes(
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

private fun NavGraphBuilder.addSeriesRoutes(navController: NavHostController) {
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

private fun NavGraphBuilder.addSearchRoutes() {
    composable(Screen.Host.route) {
        HostScreen()
    }
}

private fun NavGraphBuilder.addSettingsRoutes(navController: NavHostController) {
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
