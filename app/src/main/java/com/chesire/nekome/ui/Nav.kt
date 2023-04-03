package com.chesire.nekome.ui

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.chesire.nekome.core.flags.SeriesType

sealed class Nav {
    abstract val route: String
    abstract val destination: String

    open val args: List<NamedNavArgument> = emptyList()

    sealed class Login : Nav() {
        object Credentials : Login() {
            override val route = "credentials"
            override val destination = "credentials"
        }

        object Syncing : Login() {
            override val route = "syncing"
            override val destination = "syncing"
        }
    }

    sealed class Search : Nav() {
        object Host : Search() {
            override val route = "host"
            override val destination = "host"
        }

        object Results : Search() {
            //override val route = "results/{searchTerm}"
            override val route = "results/{searchTerm}"
            override val destination = "results"

            override val args = listOf(
                navArgument("searchTerm") { type = NavType.StringType }
            )
        }
    }

    sealed class Series : Nav() {
        object Anime : Series() {
            override val route = "anime"
            override val destination = "anime"

            override val args = listOf(
                navArgument("seriesType") {
                    type = NavType.ParcelableType(SeriesType::class.java)
                    defaultValue = SeriesType.Anime
                }
            )
        }

        object Manga : Series() {
            override val route = "manga"
            override val destination = "manga"

            override val args = listOf(
                navArgument("seriesType") {
                    type = NavType.ParcelableType(SeriesType::class.java)
                    defaultValue = SeriesType.Manga
                }
            )
        }

        object Item : Series() {
            override val route = "item/{seriesId}/{seriesTitle}"
            override val destination = "item"

            override val args = listOf(
                navArgument("seriesId") { type = NavType.IntType },
                navArgument("seriesTitle") { type = NavType.StringType }
            )
        }
    }

    sealed class Settings : Nav() {
        object Config : Settings() {
            override val route = "config"
            override val destination = "config"
        }
    }
}
