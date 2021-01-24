package com.chesire.nekome.features.search

import com.chesire.nekome.ActivityTest
import com.chesire.nekome.core.Resource
import com.chesire.nekome.helpers.creation.createSearchDomain
import com.chesire.nekome.injection.DatabaseModule
import com.chesire.nekome.injection.SearchModule
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.search.results
import com.chesire.nekome.robots.search.search
import com.chesire.nekome.search.api.SearchApi
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test

private const val DEFAULT_VALUES = "DEFAULT_VALUES"

@HiltAndroidTest
@UninstallModules(
    DatabaseModule::class,
    SearchModule::class
)
class ResultsTests : ActivityTest() {

    override val startLoggedIn = true

    @BindValue
    val searchApi = mockk<SearchApi> {
        coEvery {
            searchForAnime(DEFAULT_VALUES)
        } coAnswers {
            Resource.Success(listOf(createSearchDomain()))
        }
    }

    @Test
    fun canReachResults() {
        launchActivity()

        activity {
            goToSearch()
        }
        search {
            searchTerm(DEFAULT_VALUES)
            clickSearch()
        }
        results {
            validate { isVisible() }
        }
    }
}

