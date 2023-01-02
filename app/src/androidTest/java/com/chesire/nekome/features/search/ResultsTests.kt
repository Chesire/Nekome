package com.chesire.nekome.features.search

import androidx.compose.ui.test.junit4.createComposeRule
import com.chesire.nekome.UITest
import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.search.remote.SearchApi
import com.chesire.nekome.helpers.creation.createSearchDomain
import com.chesire.nekome.injection.SearchModule
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.search.host
import com.chesire.nekome.robots.search.results
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
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

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun canReachResults() {
        launchActivity()

        activity {
            goToSearch()
        }
        host(composeTestRule) {
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
        host(composeTestRule) {
            searchTerm(DEFAULT_VALUES)
            clickSearch()
        }
        results {
            validate { titleIs(DEFAULT_VALUES) }
        }
    }
}
