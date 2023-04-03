package com.chesire.nekome.ui

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.chesire.nekome.core.flags.SeriesType

sealed class Nav {
    abstract val route: String
    open val args: List<NamedNavArgument> = emptyList()

    sealed class Login : Nav() {
        object Credentials : Login() {
            override val route = "credentials"
        }

        object Syncing : Login() {
            override val route = "syncing"
        }
    }

    sealed class Search : Nav() {
        object Host : Search() {
            override val route = "host"
        }

        object Results : Search() {
            override val route = "results"
        }
    }

    sealed class Series : Nav() {
        object Anime : Series() {
            override val route = "anime"

            override val args = listOf(
                navArgument("seriesType") {
                    type = NavType.ParcelableType(SeriesType::class.java)
                    defaultValue = SeriesType.Anime
                }
            )
        }

        object Manga : Series() {
            override val route = "manga"

            override val args = listOf(
                navArgument("seriesType") {
                    type = NavType.ParcelableType(SeriesType::class.java)
                    defaultValue = SeriesType.Manga
                }
            )
        }

        object Item : Series() {
            override val route = "item/{seriesId}/{seriesTitle}"

            override val args = listOf(
                navArgument("seriesId") { type = NavType.IntType },
                navArgument("seriesTitle") { type = NavType.StringType }
            )
        }
    }

    sealed class Settings : Nav() {
        object Config : Settings() {
            override val route = "config"
        }
    }
}
