package com.chesire.nekome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.app.login.credentials.ui.CredentialsScreen
import com.chesire.nekome.app.search.domain.SearchModel
import com.chesire.nekome.app.search.host.ui.HostScreen
import com.chesire.nekome.app.search.results.ui.ResultsScreen
import com.chesire.nekome.app.series.collection.ui.CollectionScreen
import com.chesire.nekome.app.series.item.ui.ItemScreen
import com.chesire.nekome.app.settings.config.ui.ConfigScreen
import com.chesire.nekome.core.compose.theme.NekomeTheme
import com.chesire.nekome.core.flags.SeriesType
import dagger.hilt.android.AndroidEntryPoint

@LogLifecykle
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NekomeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    val scaffoldState = rememberScaffoldState()

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        scaffoldState = scaffoldState,
                        topBar = {
                            /* Top app bar */
                        },
                        drawerContent = {
                            /* Drawer Content */
                        }
                    ) { paddingValues ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = Nav.Series.Anime
                            ) {
                                composable(Nav.Login.Credentials) {
                                    CredentialsScreen {
                                        // TODO: Navigation
                                    }
                                }
                                composable(
                                    route = Nav.Series.Anime,
                                    arguments = listOf(
                                        navArgument("seriesType") {
                                            type = NavType.ParcelableType(SeriesType::class.java)
                                            defaultValue = SeriesType.Anime
                                        }
                                    )
                                ) {
                                    CollectionScreen { seriesId, seriesTitle ->
                                        navController.navigate("${Nav.Series.Item}/$seriesId/$seriesTitle")
                                    }
                                }
                                composable(
                                    route = Nav.Series.Manga,
                                    arguments = listOf(
                                        navArgument("seriesType") {
                                            type = NavType.ParcelableType(SeriesType::class.java)
                                            defaultValue = SeriesType.Manga
                                        }
                                    )
                                ) {
                                    CollectionScreen { seriesId, seriesTitle ->
                                        navController.navigate("${Nav.Series.Item}/$seriesId/$seriesTitle")
                                    }
                                }
                                composable(
                                    route = "${Nav.Series.Item}/{seriesId}/{seriesTitle}",
                                    arguments = listOf(
                                        navArgument("seriesId") { type = NavType.IntType },
                                        navArgument("seriesTitle") { type = NavType.StringType }
                                    )
                                ) {
                                    ItemScreen { navController.popBackStack() }
                                }
                                composable(Nav.Search.Host) {
                                    HostScreen(
                                        navigationAction = {
                                            navController.navigate("results")
                                        }
                                    )
                                }
                                composable(
                                    route = Nav.Search.Results,
                                    arguments = listOf(
                                        navArgument("searchTerm") {
                                            type = NavType.StringType
                                            defaultValue = "Test"
                                        },
                                        navArgument("searchResults") {
                                            type = NavType.ParcelableArrayType(
                                                SearchModel::class.java
                                            )
                                            defaultValue = emptyArray<SearchModel>()
                                        }
                                    )
                                ) {
                                    ResultsScreen()
                                }
                                composable(Nav.Settings.Config) {
                                    ConfigScreen {
                                        // TODO: Navigation
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
