package com.chesire.malime.view.search

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.pressImeActionButton
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.isPlatformPopup
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.chesire.malime.INVALID_SEARCH_ADD_TITLE
import com.chesire.malime.R
import com.chesire.malime.VALID_SEARCH_ADD_TITLE
import com.chesire.malime.VALID_SEARCH_SINGLE_ITEM
import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.getMalimeModel
import com.chesire.malime.injection.espressoDaggerMockRule
import com.chesire.malime.tools.ToastMatcher.Companion.onToast
import com.chesire.malime.view.MainActivity
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

@RunWith(AndroidJUnit4::class)
class SearchAddingTests {
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
    fun failureToAddAnimeProducesError() {
        val model = getMalimeModel(0, title = INVALID_SEARCH_ADD_TITLE)
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
        val model = getMalimeModel(0, title = INVALID_SEARCH_ADD_TITLE)
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
        val model = getMalimeModel(0, title = VALID_SEARCH_ADD_TITLE)
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
        val model = getMalimeModel(0, title = VALID_SEARCH_ADD_TITLE)
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
        val model = getMalimeModel(0, title = VALID_SEARCH_ADD_TITLE)
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
        val model = getMalimeModel(0, title = VALID_SEARCH_ADD_TITLE)
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
        val model = getMalimeModel(0, title = VALID_SEARCH_ADD_TITLE)
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
        val model = getMalimeModel(0, title = VALID_SEARCH_ADD_TITLE)
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
        val model = getMalimeModel(0, title = VALID_SEARCH_ADD_TITLE)
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
        val model = getMalimeModel(0, title = VALID_SEARCH_ADD_TITLE)
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
        val model = getMalimeModel(0, title = VALID_SEARCH_ADD_TITLE)
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
        val model = getMalimeModel(0, title = VALID_SEARCH_ADD_TITLE)
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

    // TODO: Add test for making sure can't add multiple times
}
