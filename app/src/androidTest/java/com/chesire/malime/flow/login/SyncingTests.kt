package com.chesire.malime.flow.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.chesire.malime.TestApplication
import com.chesire.malime.core.api.AuthApi
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
class SyncingTests {
    @get:Rule
    val loginActivity = ActivityTestRule(LoginActivity::class.java, false, false)

    @Inject
    lateinit var auth: AuthApi

    @Before
    fun setUp() {
        val app =
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApplication
        app.component.inject(this)
    }

}
