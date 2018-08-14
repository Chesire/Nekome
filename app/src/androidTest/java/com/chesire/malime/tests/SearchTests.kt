package com.chesire.malime.tests

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.clearText
import android.support.test.espresso.action.ViewActions.pressImeActionButton
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.chesire.malime.INVALID_SEARCH
import com.chesire.malime.INVALID_SEARCH_NO_ITEMS
import com.chesire.malime.R
import com.chesire.malime.tools.ToastMatcher.Companion.onToast
import com.chesire.malime.view.MainActivity
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotExist
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaRadioButtonInteractions.clickRadioButtonItem
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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
            typeText(INVALID_SEARCH_NO_ITEMS),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_no_items).check(matches(isDisplayed()))
    }

    @Test
    fun mangaSearchWithNoItemsProducesError() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(INVALID_SEARCH_NO_ITEMS),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_no_items).check(matches(isDisplayed()))
    }

    @Test
    @Ignore
    fun animeSearchIsPerformedWithAnimeSearchOption() {

    }

    @Test
    @Ignore
    fun mangaSearchIsPerformedWithMangaSearchOption() {

    }

    @Test
    @Ignore
    fun successfulSearchHasListOfSearchItems() {

    }

    @Test
    @Ignore
    fun successfulSearchCanReturnMultipleTimes() {
        // return multiple times in onNext with the observer
    }

    @Test
    @Ignore
    fun canAddAnimeItemWithCompletedStatus() {

    }

    @Test
    @Ignore
    fun canAddAnimeItemWithCurrentStatus() {

    }

    @Test
    @Ignore
    fun canAddAnimeItemWithDroppedStatus() {

    }

    @Test
    @Ignore
    fun canAddAnimeItemWithOnHoldStatus() {

    }

    @Test
    @Ignore
    fun canAddAnimeItemWithPlannedStatus() {

    }

    @Test
    @Ignore
    fun canAddMangaItemWithCompletedStatus() {

    }

    @Test
    @Ignore
    fun canAddMangaItemWithCurrentStatus() {

    }

    @Test
    @Ignore
    fun canAddMangaItemWithDroppedStatus() {

    }

    @Test
    @Ignore
    fun canAddMangaItemWithOnHoldStatus() {

    }

    @Test
    @Ignore
    fun canAddMangaItemWithPlannedStatus() {

    }

    @Test
    @Ignore
    fun itemAlreadyAddedCannotBeAddedAgain() {

    }
}