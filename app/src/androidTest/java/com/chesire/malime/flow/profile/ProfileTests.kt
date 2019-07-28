package com.chesire.malime.flow.profile

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.chesire.malime.flow.Activity
import com.chesire.malime.helpers.injector
import com.chesire.malime.kitsu.AuthProvider
import com.schibsted.spain.barista.rule.cleardata.ClearPreferencesRule
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class ProfileTests {
    @get:Rule
    val activity = ActivityTestRule(Activity::class.java, false, false)
    @get:Rule
    val clearPreferencesRule = ClearPreferencesRule()

    @Inject
    lateinit var authProvider: AuthProvider

    @Before
    fun setUp() {
        injector.inject(this)
        authProvider.accessToken = "DummyToken"
    }
}
