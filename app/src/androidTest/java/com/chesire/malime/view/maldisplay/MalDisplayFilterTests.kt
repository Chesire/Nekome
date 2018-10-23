package com.chesire.malime.view.maldisplay

import android.support.test.espresso.intent.Intents
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
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
    @Ignore
    fun filterByCurrent() {

    }

    @Test
    @Ignore
    fun filterByCompleted() {

    }

    @Test
    @Ignore
    fun filterByOnHold() {

    }

    @Test
    @Ignore
    fun filterByDropped() {

    }

    @Test
    @Ignore
    fun filterByPlanned() {

    }

    @Test
    @Ignore
    fun filterByMultiple() {

    }
}