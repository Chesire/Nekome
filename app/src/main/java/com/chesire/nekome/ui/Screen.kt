package com.chesire.nekome.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.resources.StringResource

sealed class Screen {

    abstract val route: String
    open val args: List<NamedNavArgument> = emptyList()

    companion object {
        fun showsBottomNav(route: String?): Boolean {
            return when (route) {
                Anime.route,
                Manga.route,
                Config.route -> true

                Credentials.route,
                Item.route,
                OSS.route,
                Search.route,
                Syncing.route -> false

                else -> false
            }
        }
    }

    data object Credentials : Screen() {
        override val route = "credentials"
    }

    data object Syncing : Screen() {
        override val route = "syncing"
    }

    data object Search : Screen() {
        override val route = "search"
    }

    object Anime : Screen(), BottomNavTarget {
        override val route = "anime"

        override val args = listOf(
            navArgument("seriesType") {
                type = NavType.ParcelableType(SeriesType::class.java)
                defaultValue = SeriesType.Anime
            }
        )

        override val title = StringResource.nav_anime
        override val icon = Icons.Default.VideoLibrary
        override val tag = MainActivityTags.Anime
    }

    object Manga : Screen(), BottomNavTarget {
        override val route = "manga"

        override val args = listOf(
            navArgument("seriesType") {
                type = NavType.ParcelableType(SeriesType::class.java)
                defaultValue = SeriesType.Manga
            }
        )

        override val title = StringResource.nav_manga
        override val icon = Icons.Default.CollectionsBookmark
        override val tag = MainActivityTags.Manga
    }

    data object Item : Screen() {
        override val route = "item/{seriesId}"
        const val destination = "item"

        override val args = listOf(
            navArgument("seriesId") { type = NavType.IntType }
        )
    }

    object Config : Screen(), BottomNavTarget {
        override val route = "config"
        override val title = StringResource.nav_settings
        override val icon = Icons.Default.Settings
        override val tag = MainActivityTags.Settings
    }

    data object OSS : Screen() {
        override val route = "oss"
    }
}

interface BottomNavTarget {
    @get:StringRes
    val title: Int
    val icon: ImageVector
    val tag: String
}
