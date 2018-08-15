package com.chesire.malime.tests

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
import com.chesire.malime.VALID_ADD_TITLE
import com.chesire.malime.VALID_SEARCH_MULTIPLE_ITEMS
import com.chesire.malime.VALID_SEARCH_NO_ITEMS
import com.chesire.malime.VALID_SEARCH_SINGLE_ITEM
import com.chesire.malime.tools.ToastMatcher.Companion.onToast
import com.chesire.malime.view.MainActivity
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotExist
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItemChild
import com.schibsted.spain.barista.interaction.BaristaRadioButtonInteractions.clickRadioButtonItem
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
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
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setup() {
        clickOn(R.id.menu_main_navigation_search)
    }

    @Test
    fun sortOptionShouldNotBeAvailable() {
        assertNotExist(R.id.menu_options_sort)
    }

    @Test
    fun filterOptionShouldNotBeAvailable() {
        assertNotExist(R.id.menu_options_filter)
    }

    @Test
    @Ignore
    fun canLaunchSeriesProfile() {
        // Couldn't find anything that says how this could be tested...
        // Leaving this here as it will need to be done at some point
    }

    @Test
    fun noAnimeSearchIsPerformedWithEmptySearchText() {
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
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(INVALID_SEARCH),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_general_error).check(matches(isDisplayed()))
    }

    @Test
    fun failedMangaSearchProducesError() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(INVALID_SEARCH),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_general_error).check(matches(isDisplayed()))
    }

    @Test
    fun animeSearchWithNoItemsProducesError() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_NO_ITEMS),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_no_items).check(matches(isDisplayed()))
    }

    @Test
    fun mangaSearchWithNoItemsProducesError() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_NO_ITEMS),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_no_items).check(matches(isDisplayed()))
    }

    @Test
    fun successfulAnimeSearchHasListOfSingleSearchItem() {
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
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(INVALID_SEARCH_ADD_TITLE),
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
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(INVALID_SEARCH_ADD_TITLE),
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
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_MULTIPLE_ITEMS),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 0, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_completed)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddAnimeItemWithCurrentStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_MULTIPLE_ITEMS),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 1, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_current)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddAnimeItemWithDroppedStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_MULTIPLE_ITEMS),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 2, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_dropped)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddAnimeItemWithOnHoldStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_MULTIPLE_ITEMS),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 3, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_on_hold)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddAnimeItemWithPlannedStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_MULTIPLE_ITEMS),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 4, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_planned)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddMangaItemWithCompletedStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_MULTIPLE_ITEMS),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 5, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_completed)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddMangaItemWithCurrentStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_MULTIPLE_ITEMS),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 6, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_current)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddMangaItemWithDroppedStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_MULTIPLE_ITEMS),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 7, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_dropped)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddMangaItemWithOnHoldStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_MULTIPLE_ITEMS),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 8, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_on_hold)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun canAddMangaItemWithPlannedStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_MULTIPLE_ITEMS),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 9, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_planned)).inRoot(isPlatformPopup()).perform(click())

        onToast(
            String.format(
                Locale.ROOT,
                activityRule.activity.getString(R.string.search_add_success),
                VALID_ADD_TITLE
            )
        ).check(matches(isDisplayed()))
    }
}