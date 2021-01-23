package com.chesire.nekome.features.search

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.core.Resource
import com.chesire.nekome.helpers.creation.createSearchDomain
import com.chesire.nekome.helpers.launchActivity
import com.chesire.nekome.helpers.login
import com.chesire.nekome.injection.DatabaseModule
import com.chesire.nekome.injection.SearchModule
import com.chesire.nekome.kitsu.AuthProvider
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.search.results
import com.chesire.nekome.robots.search.search
import com.chesire.nekome.search.api.SearchApi
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

private const val DEFAULT_VALUES = "DEFAULT_VALUES"

@HiltAndroidTest
@UninstallModules(
    DatabaseModule::class,
    SearchModule::class
)
@RunWith(AndroidJUnit4::class)
class ResultsTests {

    @get:Rule
    val hilt = HiltAndroidRule(this)

    @Inject
    lateinit var authProvider: AuthProvider

    @BindValue
    val searchApi = mockk<SearchApi> {
        coEvery {
            searchForAnime(DEFAULT_VALUES)
        } coAnswers {
            Resource.Success(listOf(createSearchDomain()))
        }
    }

    @Before
    fun setUp() {
        hilt.inject()
        authProvider.login()
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

