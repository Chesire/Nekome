package com.chesire.nekome.features.search

import com.chesire.nekome.UITest
import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.search.remote.SearchApi
import com.chesire.nekome.helpers.creation.createSearchDomain
import com.chesire.nekome.injection.SearchModule
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.search.results
import com.chesire.nekome.robots.search.search
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test

private const val DEFAULT_VALUES = "DEFAULT_VALUES"

@HiltAndroidTest
@UninstallModules(SearchModule::class)
class ResultsTests : UITest() {

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

    @Test
    fun resultsTitleMatchesInput() {
        launchActivity()

        activity {
            goToSearch()
        }
        search {
            searchTerm(DEFAULT_VALUES)
            clickSearch()
        }
        results {
            validate { titleIs(DEFAULT_VALUES) }
        }
    }
}
