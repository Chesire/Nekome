package com.chesire.malime.uitests

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
import com.chesire.malime.VALID_SEARCH_ADD_TITLE
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.flags.SeriesStatus
import com.chesire.malime.core.flags.Subtype
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.room.MalimeDao
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
class MalDisplayTests {
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
    fun filterByCurrent() {

    }

    @Test
    fun filterByCompleted() {

    }

    @Test
    fun filterByOnHold() {

    }

    @Test
    fun filterByDropped() {

    }

    @Test
    fun filterByPlanned() {

    }

    @Test
    fun filterByMultiple() {

    }

    @Test
    fun animeViewOnlyShowsAnimeItemType() {

    }

    @Test
    fun mangaViewOnlyShowsMangaItemType() {

    }

    private fun getMalimeModel(
        seriesId: Int = 0,
        userSeriesId: Int = 0,
        type: ItemType = ItemType.Unknown,
        subtype: Subtype = Subtype.Unknown,
        slug: String = "SERIES-SLUG",
        title: String = VALID_SEARCH_ADD_TITLE,
        seriesStatus: SeriesStatus = SeriesStatus.Unknown,
        userSeriesStatus: UserSeriesStatus = UserSeriesStatus.Unknown,
        progress: Int = 0,
        totalLength: Int = 0,
        posterImage: String = "",
        coverImage: String = "",
        nsfw: Boolean = false,
        startDate: String = "0000-00-00",
        endDate: String = "0000-00-00"
    ): MalimeModel {
        return MalimeModel(
            seriesId = seriesId,
            userSeriesId = userSeriesId,
            type = type,
            subtype = subtype,
            slug = slug,
            title = title,
            seriesStatus = seriesStatus,
            userSeriesStatus = userSeriesStatus,
            progress = progress,
            totalLength = totalLength,
            posterImage = posterImage,
            coverImage = coverImage,
            nsfw = nsfw,
            startDate = startDate,
            endDate = endDate
        )
    }
}
