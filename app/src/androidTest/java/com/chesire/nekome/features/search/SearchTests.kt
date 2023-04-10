package com.chesire.nekome.features.search

import androidx.compose.ui.test.junit4.createComposeRule
import com.chesire.nekome.UITest
import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.search.remote.SearchApi
import com.chesire.nekome.injection.SearchModule
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.search.host
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

private const val GENERIC_ERROR = "GENERIC_ERROR"
private const val NO_RESULTS_ERROR = "NO_RESULTS_ERROR"

@HiltAndroidTest
@UninstallModules(SearchModule::class)
class SearchTests : UITest() {

    @BindValue
    val searchApi = mockk<SearchApi> {
        coEvery {
            searchForAnime(GENERIC_ERROR)
        } coAnswers {
            Resource.Error("")
        }
        coEvery {
            searchForAnime(NO_RESULTS_ERROR)
        } coAnswers {
            Resource.Success(listOf())
        }
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun canReachSearch() {
        launchActivity()

        activity(composeTestRule) {
            goToSearch()
        }
        host(composeTestRule) {
            validate { isVisible() }
        }
    }

    @Test
    fun emptySearchTermShowsError() {
        launchActivity()

        activity(composeTestRule) {
            goToSearch()
        }
        host(composeTestRule) {
            searchTerm("")
            selectAnime()
            clickSearch()
        } validate {
            isEmptySearchError()
        }
    }

    @Test
    fun genericErrorFromSearchShowsError() {
        launchActivity()

        activity(composeTestRule) {
            goToSearch()
        }
        host(composeTestRule) {
            searchTerm(GENERIC_ERROR)
            selectAnime()
            clickSearch()
        } validate {
            isGenericError()
        }
    }

    @Test
    fun noSeriesFoundErrorFromSearchShowsError() {
        launchActivity()

        activity(composeTestRule) {
            goToSearch()
        }
        host(composeTestRule) {
            searchTerm(NO_RESULTS_ERROR)
            selectAnime()
            clickSearch()
        } validate {
            isNoSeriesFoundError()
        }
    }
}
