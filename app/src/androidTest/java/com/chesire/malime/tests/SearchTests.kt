package com.chesire.malime.tests

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.chesire.malime.R
import com.chesire.malime.view.MainActivity
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotExist
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
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