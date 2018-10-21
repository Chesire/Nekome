package com.chesire.malime.view.maldisplay

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.matcher.RootMatchers.isDialog
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.chesire.malime.R
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.room.MalimeDao
import com.chesire.malime.getMalimeModel
import com.chesire.malime.injection.espressoDaggerMockRule
import com.chesire.malime.view.MainActivity
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaDialogInteractions.clickDialogPositiveButton
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu
import io.reactivex.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class MalDisplaySortTests {
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
    @Ignore("Need to know how default sort works, think its based on ID?")
    fun sortByDefault() {

    }

    @Test
    fun sortByTitle() {
        val models = listOf(
            getMalimeModel(seriesId = 0, title = "B", type = ItemType.Anime),
            getMalimeModel(seriesId = 1, title = "E", type = ItemType.Anime),
            getMalimeModel(seriesId = 2, title = "A", type = ItemType.Anime),
            getMalimeModel(seriesId = 3, title = "C", type = ItemType.Anime),
            getMalimeModel(seriesId = 4, title = "F", type = ItemType.Anime)
        )
        `when`(mockDao.getAll()).thenReturn(Flowable.just(models))

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_anime)

        clickMenu(R.id.menu_options_sort)
        onView(withText(R.string.sort_choice_title))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        clickDialogPositiveButton()


    }

    @Test
    fun sortByStartDate() {

    }

    @Test
    fun sortByEndDate() {

    }

    @Test
    fun animeViewOnlyShowsAnimeItemType() {

    }

    @Test
    fun mangaViewOnlyShowsMangaItemType() {

    }
}
