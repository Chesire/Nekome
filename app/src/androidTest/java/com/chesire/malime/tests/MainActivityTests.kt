package com.chesire.malime.tests

import android.support.design.internal.BottomNavigationItemView
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.chesire.malime.R
import com.chesire.malime.view.MainActivity
import com.chesire.malime.view.preferences.PrefActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTests {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun canNavigateToAnimeView() {
        onView(withId(R.id.menu_main_navigation_anime)).perform(click())

        onView(withId(R.id.menu_main_navigation_anime)).check(
            matches(
                withBottomNavItemCheckedStatus(true)
            )
        )
        onView(withId(R.id.menu_main_navigation_manga)).check(
            matches(
                withBottomNavItemCheckedStatus(false)
            )
        )
        onView(withId(R.id.menu_main_navigation_search)).check(
            matches(
                withBottomNavItemCheckedStatus(false)
            )
        )
        onView(withId(R.id.maldisplay_layout)).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun canNavigateToMangaView() {
        onView(withId(R.id.menu_main_navigation_manga)).perform(click())

        onView(withId(R.id.menu_main_navigation_anime)).check(
            matches(
                withBottomNavItemCheckedStatus(false)
            )
        )
        onView(withId(R.id.menu_main_navigation_manga)).check(
            matches(
                withBottomNavItemCheckedStatus(true)
            )
        )
        onView(withId(R.id.menu_main_navigation_search)).check(
            matches(
                withBottomNavItemCheckedStatus(false)
            )
        )
        onView(withId(R.id.maldisplay_layout)).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun canNavigateToSearchView() {
        onView(withId(R.id.menu_main_navigation_search)).perform(click())

        onView(withId(R.id.menu_main_navigation_anime)).check(
            matches(
                withBottomNavItemCheckedStatus(false)
            )
        )
        onView(withId(R.id.menu_main_navigation_manga)).check(
            matches(
                withBottomNavItemCheckedStatus(false)
            )
        )
        onView(withId(R.id.menu_main_navigation_search)).check(
            matches(
                withBottomNavItemCheckedStatus(true)
            )
        )
        onView(withId(R.id.search_layout)).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun canLaunchProfile() {
        // Couldn't find anything that says how this could be tested...
        // Leaving this here as it will need to be done at some point
    }

    @Test
    fun canLaunchPreferencesFromMalDisplay() {
        Intents.init()
        onView(withId(R.id.menu_main_navigation_anime)).perform(click())
        openActionBarOverflowOrOptionsMenu(activityRule.activity)
        onView(withText(R.string.options_settings)).perform(click())

        Intents.intended(IntentMatchers.hasComponent(PrefActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun canLaunchPreferencesFromSearch() {
        Intents.init()
        onView(withId(R.id.menu_main_navigation_search)).perform(click())
        openActionBarOverflowOrOptionsMenu(activityRule.activity)
        onView(withText(R.string.options_settings)).perform(click())

        Intents.intended(IntentMatchers.hasComponent(PrefActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun cancellingLogout() {

    }

    @Test
    fun executingLogoutGoesBackToLogin() {

    }

    private fun withBottomNavItemCheckedStatus(isChecked: Boolean): Matcher<View> {
        return object :
            BoundedMatcher<View, BottomNavigationItemView>(BottomNavigationItemView::class.java) {
            var triedMatching: Boolean = false

            override fun describeTo(description: Description) {
                if (triedMatching) {
                    description.appendText("with BottomNavigationItem check status: " + isChecked.toString())
                    description.appendText("But was: " + (!isChecked).toString())
                }
            }

            override fun matchesSafely(item: BottomNavigationItemView): Boolean {
                triedMatching = true
                return item.itemData.isChecked == isChecked
            }
        }
    }
}