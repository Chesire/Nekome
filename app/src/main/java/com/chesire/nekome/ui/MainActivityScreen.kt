package com.chesire.nekome.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chesire.nekome.R
import kotlinx.coroutines.launch

@Composable
fun MainActivityScreen(viewModel: MainActivityViewModel = viewModel()) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .semantics { testTag = MainActivityTags.Root }
    ) {
        val state = viewModel.uiState.collectAsState()
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.semantics { testTag = MainActivityTags.Snackbar }
                )
            },
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                if (Screen.showsBottomNav(navBackStackEntry?.destination?.route)) {
                    NavigationBar(tonalElevation = 0.dp) {
                        val currentDestination = navBackStackEntry?.destination
                        bottomNavRoutes.forEach { screen ->
                            check(screen is Screen)
                            NavigationBarItem(
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
            }
        ) { paddingValues ->
            if (state.value.isInitialized) {
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

        val snackbarString = stringResource(id = R.string.logout_forced)
        LaunchedEffect(state.value.kickUserToLogin) {
            if (state.value.kickUserToLogin != null) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(snackbarString)
                }

                navController.navigate(Screen.Credentials.route)
                viewModel.observeKickUserToLogin()
            }
        }
    }
}

object MainActivityTags {
    const val Root = "MainActivityRoot"
    const val Snackbar = "MainActivitySnackbar"
    const val Anime = "MainActivityAnime"
    const val Manga = "MainActivityManga"
    const val Search = "MainActivitySearch"
    const val Settings = "MainActivitySettings"
}
