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
import com.chesire.malime.R
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

    }

    @Test
    fun failureToAddMangaProducesError() {

    }

    @Test
    @Ignore
    fun canAddAnimeItemWithCompletedStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(VALID_SEARCH_MULTIPLE_ITEMS),
            pressImeActionButton()
        )

        sleep(100)
        clickListItemChild(R.id.search_all_items, 0, R.id.search_image_add_button)
        onView(withText(R.string.filter_state_completed)).inRoot(isPlatformPopup()).perform(click())
    }

    @Test
    @Ignore
    fun canAddAnimeItemWithCurrentStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)

    }

    @Test
    @Ignore
    fun canAddAnimeItemWithDroppedStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)

    }

    @Test
    @Ignore
    fun canAddAnimeItemWithOnHoldStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)

    }

    @Test
    @Ignore
    fun canAddAnimeItemWithPlannedStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)

    }

    @Test
    @Ignore
    fun canAddMangaItemWithCompletedStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)

    }

    @Test
    @Ignore
    fun canAddMangaItemWithCurrentStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)

    }

    @Test
    @Ignore
    fun canAddMangaItemWithDroppedStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)

    }

    @Test
    @Ignore
    fun canAddMangaItemWithOnHoldStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)

    }

    @Test
    @Ignore
    fun canAddMangaItemWithPlannedStatus() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)

    }

    @Test
    @Ignore
    fun itemAlreadyAddedCannotBeAddedAgain() {

    }
}