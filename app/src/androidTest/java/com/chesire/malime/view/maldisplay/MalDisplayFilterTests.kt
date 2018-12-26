package com.chesire.malime.view.maldisplay

import androidx.test.espresso.intent.Intents
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.chesire.malime.core.room.MalimeDao
import com.chesire.malime.injection.espressoDaggerMockRule
import com.chesire.malime.view.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock

@RunWith(AndroidJUnit4::class)
class MalDisplayFilterTests {
    @get:Rule
    var daggerRule = espressoDaggerMockRule()

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Mock
    lateinit var mockDao: MalimeDao

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    @Ignore("Not yet implemented")
    fun filterByCurrent() {
    }

    @Test
    @Ignore("Not yet implemented")
    fun filterByCompleted() {
    }

    @Test
    @Ignore("Not yet implemented")
    fun filterByOnHold() {
    }

    @Test
    @Ignore("Not yet implemented")
    fun filterByDropped() {
    }

    @Test
    @Ignore("Not yet implemented")
    fun filterByPlanned() {
    }

    @Test
    @Ignore("Not yet implemented")
    fun filterByMultiple() {
    }
}
