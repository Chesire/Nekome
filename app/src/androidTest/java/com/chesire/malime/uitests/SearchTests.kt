package com.chesire.malime.uitests

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.clearText
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.pressImeActionButton
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.isPlatformPopup
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.chesire.malime.INVALID_SEARCH
import com.chesire.malime.INVALID_SEARCH_ADD_TITLE
import com.chesire.malime.R
import com.chesire.malime.VALID_SEARCH_ADD_TITLE
import com.chesire.malime.VALID_SEARCH_MULTIPLE_ITEMS
import com.chesire.malime.VALID_SEARCH_NO_ITEMS
import com.chesire.malime.VALID_SEARCH_SINGLE_ITEM
import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.flags.SeriesStatus
import com.chesire.malime.core.flags.Subtype
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.injection.espressoDaggerMockRule
import com.chesire.malime.tools.ToastMatcher.Companion.onToast
import com.chesire.malime.view.MainActivity
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotExist
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItemChild
import com.schibsted.spain.barista.interaction.BaristaRadioButtonInteractions.clickRadioButtonItem
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep
import io.reactivex.Single
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import java.util.Locale

/**
 * Requirements:
 *
 * Sleep is used to ensure the recycler view is visible, as the progress dialog can hide it.
 * Animations are required to be turned off for the device.
 */
@RunWith(AndroidJUnit4::class)
class SearchTests {
    @get:Rule
    var daggerRule = espressoDaggerMockRule()

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Mock
    private lateinit var searchApi: SearchApi
    @Mock
    private lateinit var libraryApi: LibraryApi

    @Test
    fun sortOptionShouldNotBeAvailable() {
        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        assertNotExist(R.id.menu_options_sort)
    }

    @Test
    fun filterOptionShouldNotBeAvailable() {
        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        assertNotExist(R.id.menu_options_filter)
    }

    @Test
    fun noAnimeSearchIsPerformedWithEmptySearchText() {
        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            clearText(),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_no_items).check(doesNotExist())
        onToast(R.string.search_failed_general_error).check(doesNotExist())
    }

    @Test
    fun noMangaSearchIsPerformedWithEmptySearchText() {
        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            clearText(),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_no_items).check(doesNotExist())
        onToast(R.string.search_failed_general_error).check(doesNotExist())
    }

    @Test
    fun failedAnimeSearchProducesError() {
        `when`(
            searchApi.searchForSeriesWith(
                INVALID_SEARCH,
                ItemType.Anime
            )
        ).thenReturn(Single.error { Throwable("failedAnimeSearchProducesError") })

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(INVALID_SEARCH),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_general_error).check(matches(isDisplayed()))
    }

    @Test
    fun failedMangaSearchProducesError() {
        `when`(
            searchApi.searchForSeriesWith(
                INVALID_SEARCH,
                ItemType.Manga
            )
        ).thenReturn(Single.error { Throwable("failedAnimeSearchProducesError") })

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(INVALID_SEARCH),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_general_error).check(matches(isDisplayed()))
    }

    @Test
    fun animeSearchWithNoItemsProducesError() {
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_NO_ITEMS,
                ItemType.Anime
            )
        ).thenReturn(Single.just(listOf()))

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_NO_ITEMS),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_no_items).check(matches(isDisplayed()))
    }

    @Test
    fun mangaSearchWithNoItemsProducesError() {
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_NO_ITEMS,
                ItemType.Manga
            )
        ).thenReturn(Single.just(listOf()))

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_NO_ITEMS),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_no_items).check(matches(isDisplayed()))
    }

    @Test
    fun successfulAnimeSearchHasListOfSingleSearchItem() {
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_SINGLE_ITEM,
                ItemType.Anime
            )
        ).thenReturn(Single.just(listOf(getMalimeModel())))

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_SINGLE_ITEM),
            pressImeActionButton()
        )

        sleep(100)
        assertRecyclerViewItemCount(R.id.search_all_items, 1)
    }

    @Test
    fun successfulMangaSearchHasListOfSingleSearchItem() {
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_SINGLE_ITEM,
                ItemType.Manga
            )
        ).thenReturn(Single.just(listOf(getMalimeModel())))

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_SINGLE_ITEM),
            pressImeActionButton()
        )

        sleep(100)
        assertRecyclerViewItemCount(R.id.search_all_items, 1)
    }

    @Test
    fun successfulAnimeSearchHasListOfSearchItems() {
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_MULTIPLE_ITEMS,
                ItemType.Anime
            )
        ).thenReturn(
            Single.just(
                listOf(
                    getMalimeModel(seriesId = 0),
                    getMalimeModel(seriesId = 1),
                    getMalimeModel(seriesId = 2),
                    getMalimeModel(seriesId = 3),
                    getMalimeModel(seriesId = 4),
                    getMalimeModel(seriesId = 5),
                    getMalimeModel(seriesId = 6),
                    getMalimeModel(seriesId = 7),
                    getMalimeModel(seriesId = 8),
                    getMalimeModel(seriesId = 9),
                    getMalimeModel(seriesId = 10)
                )
            )
        )

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_MULTIPLE_ITEMS),
            pressImeActionButton()
        )

        sleep(100)
        assertRecyclerViewItemCount(R.id.search_all_items, 11)
    }

    @Test
    fun successfulMangaSearchHasListOfSearchItems() {
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_MULTIPLE_ITEMS,
                ItemType.Manga
            )
        ).thenReturn(
            Single.just(
                listOf(
                    getMalimeModel(seriesId = 0),
                    getMalimeModel(seriesId = 1),
                    getMalimeModel(seriesId = 2),
                    getMalimeModel(seriesId = 3),
                    getMalimeModel(seriesId = 4),
                    getMalimeModel(seriesId = 5),
                    getMalimeModel(seriesId = 6),
                    getMalimeModel(seriesId = 7),
                    getMalimeModel(seriesId = 8),
                    getMalimeModel(seriesId = 9),
                    getMalimeModel(seriesId = 10)
                )
            )
        )

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_MULTIPLE_ITEMS),
            pressImeActionButton()
        )

        sleep(100)
        assertRecyclerViewItemCount(R.id.search_all_items, 11)
    }

    @Test
    fun failureToAddAnimeProducesError() {
        val model = getMalimeModel(title = INVALID_SEARCH_ADD_TITLE)
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_SINGLE_ITEM,
                ItemType.Anime
            )
        ).thenReturn(Single.just(listOf(model)))
        `when`(libraryApi.addItem(model)).thenReturn(Single.error { Throwable("failureToAddAnimeProducesError") })

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_SINGLE_ITEM),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 0, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_completed)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_failed),
                INVALID_SEARCH_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun failureToAddMangaProducesError() {
        val model = getMalimeModel(title = INVALID_SEARCH_ADD_TITLE)
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_SINGLE_ITEM,
                ItemType.Manga
            )
        ).thenReturn(Single.just(listOf(model)))
        `when`(libraryApi.addItem(model)).thenReturn(Single.error { Throwable("failureToAddMangaProducesError") })

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_SINGLE_ITEM),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 0, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_completed)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_failed),
                INVALID_SEARCH_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddAnimeItemWithCompletedStatus() {
        val model = getMalimeModel(title = VALID_SEARCH_ADD_TITLE)
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_SINGLE_ITEM,
                ItemType.Anime
            )
        ).thenReturn(Single.just(listOf(model)))
        `when`(libraryApi.addItem(model)).thenReturn(Single.just(model))

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_SINGLE_ITEM),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 0, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_completed)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_SEARCH_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddAnimeItemWithCurrentStatus() {
        val model = getMalimeModel(title = VALID_SEARCH_ADD_TITLE)
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_SINGLE_ITEM,
                ItemType.Anime
            )
        ).thenReturn(Single.just(listOf(model)))
        `when`(libraryApi.addItem(model)).thenReturn(Single.just(model))

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_SINGLE_ITEM),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 0, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_current)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_SEARCH_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddAnimeItemWithDroppedStatus() {
        val model = getMalimeModel(title = VALID_SEARCH_ADD_TITLE)
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_SINGLE_ITEM,
                ItemType.Anime
            )
        ).thenReturn(Single.just(listOf(model)))
        `when`(libraryApi.addItem(model)).thenReturn(Single.just(model))

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_SINGLE_ITEM),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 0, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_dropped)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_SEARCH_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddAnimeItemWithOnHoldStatus() {
        val model = getMalimeModel(title = VALID_SEARCH_ADD_TITLE)
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_SINGLE_ITEM,
                ItemType.Anime
            )
        ).thenReturn(Single.just(listOf(model)))
        `when`(libraryApi.addItem(model)).thenReturn(Single.just(model))

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_SINGLE_ITEM),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 0, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_on_hold)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_SEARCH_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddAnimeItemWithPlannedStatus() {
        val model = getMalimeModel(title = VALID_SEARCH_ADD_TITLE)
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_SINGLE_ITEM,
                ItemType.Anime
            )
        ).thenReturn(Single.just(listOf(model)))
        `when`(libraryApi.addItem(model)).thenReturn(Single.just(model))

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_SINGLE_ITEM),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 0, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_planned)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_SEARCH_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddMangaItemWithCompletedStatus() {
        val model = getMalimeModel(title = VALID_SEARCH_ADD_TITLE)
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_SINGLE_ITEM,
                ItemType.Manga
            )
        ).thenReturn(Single.just(listOf(model)))
        `when`(libraryApi.addItem(model)).thenReturn(Single.just(model))

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_SINGLE_ITEM),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 0, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_completed)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_SEARCH_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddMangaItemWithCurrentStatus() {
        val model = getMalimeModel(title = VALID_SEARCH_ADD_TITLE)
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_SINGLE_ITEM,
                ItemType.Manga
            )
        ).thenReturn(Single.just(listOf(model)))
        `when`(libraryApi.addItem(model)).thenReturn(Single.just(model))

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_SINGLE_ITEM),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 0, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_current)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_SEARCH_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddMangaItemWithDroppedStatus() {
        val model = getMalimeModel(title = VALID_SEARCH_ADD_TITLE)
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_SINGLE_ITEM,
                ItemType.Manga
            )
        ).thenReturn(Single.just(listOf(model)))
        `when`(libraryApi.addItem(model)).thenReturn(Single.just(model))

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_SINGLE_ITEM),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 0, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_dropped)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_SEARCH_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddMangaItemWithOnHoldStatus() {
        val model = getMalimeModel(title = VALID_SEARCH_ADD_TITLE)
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_SINGLE_ITEM,
                ItemType.Manga
            )
        ).thenReturn(Single.just(listOf(model)))
        `when`(libraryApi.addItem(model)).thenReturn(Single.just(model))

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_SINGLE_ITEM),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 0, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_on_hold)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_SEARCH_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddMangaItemWithPlannedStatus() {
        val model = getMalimeModel(title = VALID_SEARCH_ADD_TITLE)
        `when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_SINGLE_ITEM,
                ItemType.Manga
            )
        ).thenReturn(Single.just(listOf(model)))
        `when`(libraryApi.addItem(model)).thenReturn(Single.just(model))

        activityRule.launchActivity(null)
        clickOn(R.id.menu_main_navigation_search)
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_SINGLE_ITEM),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 0, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_planned)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_SEARCH_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    @Ignore
    fun canLaunchSeriesProfile() {
        // Couldn't find anything that says how this could be tested...
        // Leaving this here as it will need to be done at some point
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