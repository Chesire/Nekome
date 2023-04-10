package com.chesire.nekome.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.chesire.nekome.R
import com.chesire.nekome.core.flags.SeriesType

sealed class Screen {

    abstract val route: String
    open val args: List<NamedNavArgument> = emptyList()

    companion object {
        fun showsBottomNav(route: String?): Boolean {
            return when (route) {
                Anime.route,
                Manga.route,
                Host.route,
                Config.route -> true

                Credentials.route,
                Item.route,
                OSS.route,
                Syncing.route -> false

                else -> false
            }
        }
    }

    object Credentials : Screen() {
        override val route = "credentials"
    }

    object Syncing : Screen() {
        override val route = "syncing"
    }

    object Host : Screen(), BottomNavTarget {
        override val route = "host"
        override val title = R.string.nav_search
        override val icon = R.drawable.ic_search
        override val tag = MainActivityTags.Search
    }

    object Anime : Screen(), BottomNavTarget {
        override val route = "anime"

        override val args = listOf(
            navArgument("seriesType") {
                type = NavType.ParcelableType(SeriesType::class.java)
                defaultValue = SeriesType.Anime
            }
        )

        override val title = R.string.nav_anime
        override val icon = R.drawable.ic_video_library
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

        override val title = R.string.nav_manga
        override val icon = R.drawable.ic_collections_bookmark
        override val tag = MainActivityTags.Manga
    }

    object Item : Screen() {
        override val route = "item/{seriesId}/{seriesTitle}"
        const val destination = "item"

        override val args = listOf(
            navArgument("seriesId") { type = NavType.IntType },
            navArgument("seriesTitle") { type = NavType.StringType }
        )
    }

    object Config : Screen(), BottomNavTarget {
        override val route = "config"
        override val title = R.string.nav_settings
        override val icon = R.drawable.ic_settings
        override val tag = MainActivityTags.Settings
    }

    object OSS : Screen() {
        override val route = "oss"
    }
}

interface BottomNavTarget {
    @get:StringRes
    val title: Int

    @get:DrawableRes
    val icon: Int // TODO: Change these to compose icons

    val tag: String
}
