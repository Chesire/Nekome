package com.chesire.nekome.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun MainActivityScreen(viewModel: MainActivityViewModel = viewModel()) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .semantics { testTag = MainActivityTags.Root }
    ) {
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
                        bottomNavRoutes.forEach { screen ->
                            check(screen is Screen)
                            BottomNavigationItem(
                                icon = {
                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = stringResource(id = screen.title)
                                    )
                                },
                                modifier = Modifier.semantics { testTag = screen.tag },
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

object MainActivityTags {
    const val Root = "MainActivityRoot"
    const val Anime = "MainActivityAnime"
    const val Manga = "MainActivityManga"
    const val Search = "MainActivitySearch"
    const val Settings = "MainActivitySettings"
}
