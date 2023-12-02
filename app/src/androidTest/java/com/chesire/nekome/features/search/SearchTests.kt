package com.chesire.nekome.features.search

import com.chesire.nekome.UITest
import com.chesire.nekome.core.models.ErrorDomain
import com.chesire.nekome.datasource.search.remote.SearchApi
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.search.search
import com.chesire.nekome.robots.series.seriesList
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import javax.inject.Inject
import org.junit.Before
import org.junit.Test

private const val GENERIC_ERROR = "GENERIC_ERROR"
private const val NO_RESULTS_ERROR = "NO_RESULTS_ERROR"

@HiltAndroidTest
class SearchTests : UITest() {

    @Inject
    lateinit var searchApi: SearchApi

    @Before
    fun setup() {
        coEvery {
            searchApi.searchForAnime(GENERIC_ERROR)
        } coAnswers {
            Err(ErrorDomain.badRequest)
        }
        coEvery {
            searchApi.searchForAnime(NO_RESULTS_ERROR)
        } coAnswers {
            Ok(listOf())
        }
    }

    @Test
    fun canReachSearch() {
        launchActivity()

        activity(composeTestRule) {
            goToAnime()
        }
        seriesList(composeTestRule) {
            goToSearch()
        }
        search(composeTestRule) {
            validate { isVisible() }
        }
    }

    @Test
    fun emptySearchTermShowsError() {
        launchActivity()

        activity(composeTestRule) {
            goToAnime()
        }
        seriesList(composeTestRule) {
            goToSearch()
        }
        search(composeTestRule) {
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
            goToAnime()
        }
        seriesList(composeTestRule) {
            goToSearch()
        }
        search(composeTestRule) {
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
            goToAnime()
        }
        seriesList(composeTestRule) {
            goToSearch()
        }
        search(composeTestRule) {
            searchTerm(NO_RESULTS_ERROR)
            selectAnime()
            clickSearch()
        } validate {
            isNoSeriesFoundError()
        }
    }
}
