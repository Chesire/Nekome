package com.chesire.nekome

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adevinta.android.barista.rule.cleardata.ClearDatabaseRule
import com.adevinta.android.barista.rule.cleardata.ClearPreferencesRule
import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.database.dao.UserDao
import com.chesire.nekome.datasource.auth.local.AuthProvider
import com.chesire.nekome.helpers.createTestUser
import com.chesire.nekome.helpers.login
import com.chesire.nekome.helpers.logout
import com.chesire.nekome.helpers.reset
import dagger.hilt.android.testing.HiltAndroidRule
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * Provides a base class to use for all UI tests.
 */
@RunWith(AndroidJUnit4::class)
abstract class UITest {

    @Suppress("LeakingThis")
    @get:Rule
    val hilt = HiltAndroidRule(this)

    @get:Rule
    val clearDatabase = ClearDatabaseRule()

    @get:Rule
    val clearPreferences = ClearPreferencesRule()

    @Inject
    lateinit var authProvider: AuthProvider

    @Inject
    lateinit var series: SeriesDao

    @Inject
    lateinit var user: UserDao

    @Inject
    lateinit var seriesPreferences: SeriesPreferences

    /**
     * Flag for if the test should start with a logged in user.
     * Defaults to `true`, override to force the user to be logged out.
     */
    open val startLoggedIn: Boolean = true

    /**
     * Initial setup method.
     */
    @Before
    open fun setUp() {
        hilt.inject()

        runBlocking {
            seriesPreferences.reset()
        }

        if (startLoggedIn) {
            authProvider.login()
            user.createTestUser()
        } else {
            authProvider.logout()
        }
    }

    /**
     * Launches the [Activity] using the [ActivityScenario].
     */
    protected fun launchActivity() {
        ActivityScenario.launch(Activity::class.java)
        // Not the nicest solution, but it keeps compose views a bit happier when they launch.
        Thread.sleep(200)
    }
}
