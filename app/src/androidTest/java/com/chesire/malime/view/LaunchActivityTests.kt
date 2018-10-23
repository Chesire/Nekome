package com.chesire.malime.view

import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.chesire.malime.LaunchActivity
import com.chesire.malime.core.repositories.Authorization
import com.chesire.malime.injection.espressoDaggerMockRule
import com.chesire.malime.view.login.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class LaunchActivityTests {
    @get:Rule
    var daggerRule = espressoDaggerMockRule()

    @get:Rule
    val activityRule = ActivityTestRule(LaunchActivity::class.java, false, false)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Mock
    private lateinit var authorization: Authorization

    @Test
    fun whenUserNotLoggedInLaunchLoginActivity() {
        `when`(authorization.hasLoggedIn()).thenReturn(false)
        activityRule.launchActivity(null)

        intended(hasComponent(LoginActivity::class.java.name))
    }

    @Test
    fun whenUserIsLoggedInLaunchMainActivity() {
        `when`(authorization.hasLoggedIn()).thenReturn(true)
        activityRule.launchActivity(null)

        intended(hasComponent(MainActivity::class.java.name))
    }
}