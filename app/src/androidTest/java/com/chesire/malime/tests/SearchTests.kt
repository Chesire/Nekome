package com.chesire.malime.tests

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.pressImeActionButton
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.chesire.malime.INVALID_SEARCH
import com.chesire.malime.R
import com.chesire.malime.view.MainActivity
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotExist
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaRadioButtonInteractions.clickRadioButtonItem
import org.hamcrest.Matchers.not
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
    @Ignore
    fun noSearchIsPerformedWithEmptySearchText() {
        // nothing will come up, so not sure how to check it currently
    }

    @Test
    fun failedAnimeSearchProducesError() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(INVALID_SEARCH),
            pressImeActionButton()
        )

        onView(withText(R.string.search_failed_general_error))
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun failedMangaSearchProducesError() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(INVALID_SEARCH),
            pressImeActionButton()
        )

        onView(withText(R.string.search_failed_general_error))
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun failedAnimeSearchNoItemsProducesError() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_anime_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(INVALID_SEARCH),
            pressImeActionButton()
        )

        onView(withText(R.string.search_failed_no_items))
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun failedMangaSearchNoItemsProducesError() {
        clickRadioButtonItem(R.id.search_option_choices, R.id.search_option_manga_choice)
        onView(withId(R.id.search_search_term_edit_text)).perform(
            typeText(INVALID_SEARCH),
            pressImeActionButton()
        )

        onView(withText(R.string.search_failed_no_items))
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun animeSearchIsPerformedWithAnimeSearchOption() {

    }

    @Test
    fun mangaSearchIsPerformedWithAnimeSearchOption() {

    }

    @Test
    fun successfulSearchHasListOfSearchItems() {

    }

    @Test
    fun canAddAnimeItemWithCompletedStatus() {

    }

    @Test
    fun canAddAnimeItemWithCurrentStatus() {

    }

    @Test
    fun canAddAnimeItemWithDroppedStatus() {

    }

    @Test
    fun canAddAnimeItemWithOnHoldStatus() {

    }

    @Test
    fun canAddAnimeItemWithPlannedStatus() {

    }

    @Test
    fun canAddMangaItemWithCompletedStatus() {

    }

    @Test
    fun canAddMangaItemWithCurrentStatus() {

    }

    @Test
    fun canAddMangaItemWithDroppedStatus() {

    }

    @Test
    fun canAddMangaItemWithOnHoldStatus() {

    }

    @Test
    fun canAddMangaItemWithPlannedStatus() {

    }

    @Test
    fun itemAlreadyAddedCannotBeAddedAgain() {

    }
}