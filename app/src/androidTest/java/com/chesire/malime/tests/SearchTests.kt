package com.chesire.malime.tests

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.chesire.malime.R
import com.chesire.malime.view.MainActivity
import org.junit.After
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
        // Navigate to search view
        onView(withId(R.id.menu_main_navigation_search)).perform(click())
    }

    @After
    fun teardown() {
        // To be implemented
    }

    @Test
    fun sortOptionShouldNotBeAvailable() {
        onView(withId(R.id.menu_options_sort)).check(doesNotExist())
    }

    @Test
    fun filterOptionShouldNotBeAvailable() {
        onView(withId(R.id.menu_options_filter)).check(doesNotExist())
    }

    @Test
    @Ignore
    fun canLaunchSeriesProfile() {
        // Couldn't find anything that says how this could be tested...
        // Leaving this here as it will need to be done at some point
    }

    @Test
    fun noSearchIsPerformedWithEmptySearchText() {

    }

    @Test
    fun animeSearchIsPerformedWithAnimeSearchOption() {

    }

    @Test
    fun mangaSearchIsPerformedWithAnimeSearchOption() {

    }

    @Test
    fun failedSearchProducesError() {

    }

    @Test
    fun successfulSearchHasListOfSearchItems() {

    }

    @Test
    fun canAddItemWithCompletedStatus() {

    }

    @Test
    fun canAddItemWithCurrentStatus() {

    }

    @Test
    fun canAddItemWithDroppedStatus() {

    }

    @Test
    fun canAddItemWithOnHoldStatus() {

    }

    @Test
    fun canAddItemWithPlannedStatus() {

    }

    @Test
    fun itemAlreadyAddedCannotBeAddedAgain() {

    }
}