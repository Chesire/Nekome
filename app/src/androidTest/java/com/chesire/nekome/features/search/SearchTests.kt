package com.chesire.nekome.features.search

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.helpers.launchActivity
import com.chesire.nekome.helpers.login
import com.chesire.nekome.injection.DatabaseModule
import com.chesire.nekome.kitsu.AuthProvider
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.search.search
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
@RunWith(AndroidJUnit4::class)
class SearchTests {

    @get:Rule
    val hilt = HiltAndroidRule(this)

    @Inject
    lateinit var authProvider: AuthProvider

    @Before
    fun setUp() {
        hilt.inject()
        authProvider.login()
    }

    @Test
    fun canReachSearch() {
        launchActivity()

        activity {
            goToSearch()
        }
        search {
            validate { isVisible() }
        }
    }
}
