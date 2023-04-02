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
import com.chesire.nekome.app.search.host.ui.HostScreen
import com.chesire.nekome.app.series.collection.ui.CollectionScreen
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
                                startDestination = "anime"
                            ) {
                                composable("login") {
                                    CredentialsScreen {
                                        // TODO: Implement the navigation to the sub screen here?
                                        // TODO: Probably better inside the module
                                    }
                                }
                                composable(
                                    route = "anime",
                                    arguments = listOf(
                                        navArgument("seriesType") {
                                            type = NavType.ParcelableType(SeriesType::class.java)
                                            defaultValue = SeriesType.Anime
                                        }
                                    )
                                ) {
                                    CollectionScreen { seriesId, seriesTitle ->
                                        // TODO: Implement the navigation to the sub screen here?
                                        // TODO: Probably better inside the module
                                    }
                                }
                                composable(
                                    route = "manga",
                                    arguments = listOf(
                                        navArgument("seriesType") {
                                            type = NavType.ParcelableType(SeriesType::class.java)
                                            defaultValue = SeriesType.Manga
                                        }
                                    )
                                ) {
                                    CollectionScreen { seriesId, seriesTitle ->
                                        // TODO: Implement the navigation to the sub screen here?
                                        // TODO: Probably better inside the module
                                    }
                                }
                                composable("search") {
                                    HostScreen {
                                        // TODO: Navigation action can be handled better now
                                    }
                                }
                                composable("settings") {
                                    ConfigScreen {
                                        // TODO: Implement the navigation to the sub screen here?
                                        // TODO: Probably better inside the module
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
