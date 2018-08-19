package com.chesire.malime.uitests

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.clearText
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.pressImeActionButton
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.chesire.malime.INVALID_PASSWORD
import com.chesire.malime.INVALID_USERNAME
import com.chesire.malime.R
import com.chesire.malime.VALID_PASSWORD
import com.chesire.malime.VALID_USERNAME
import com.chesire.malime.view.MainActivity
import com.chesire.malime.view.login.LoginActivity
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep
import org.hamcrest.CoreMatchers.not
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTests {
    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun displayErrorForBlankUsername() {
        onView(withId(R.id.login_username_edit_text)).perform(clearText())
        onView(withId(R.id.login_password_edit_text)).perform(
            typeText(VALID_PASSWORD),
            closeSoftKeyboard()
        )

        onView(withId(R.id.login_button)).perform(click())

        onView(withText(R.string.login_failure_email))
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun displayErrorForBlankPassword() {
        onView(withId(R.id.login_username_edit_text)).perform(typeText(VALID_USERNAME))
        onView(withId(R.id.login_password_edit_text)).perform(clearText(), closeSoftKeyboard())

        onView(withId(R.id.login_button)).perform(click())

        onView(withText(R.string.login_failure_password))
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun displayErrorForInvalidUsername() {
        onView(withId(R.id.login_username_edit_text)).perform(typeText(INVALID_USERNAME))
        onView(withId(R.id.login_password_edit_text)).perform(
            typeText(VALID_PASSWORD),
            closeSoftKeyboard()
        )

        onView(withId(R.id.login_button)).perform(click())

        onView(withText(R.string.login_failure))
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun displayErrorForInvalidPassword() {
        onView(withId(R.id.login_username_edit_text)).perform(typeText(VALID_USERNAME))
        onView(withId(R.id.login_password_edit_text)).perform(
            typeText(INVALID_PASSWORD),
            closeSoftKeyboard()
        )

        onView(withId(R.id.login_button)).perform(click())

        onView(withText(R.string.login_failure))
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun canNavigateFieldsWithIMEButton() {
        onView(withId(R.id.login_username_edit_text)).perform(clearText(), pressImeActionButton())
        onView(withId(R.id.login_password_edit_text)).perform(clearText(), pressImeActionButton())

        onView(withText(R.string.login_failure_email))
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun correctDetailsLaunchesMainActivity() {
        Intents.init()

        onView(withId(R.id.login_username_edit_text)).perform(
            typeText(VALID_USERNAME),
            pressImeActionButton()
        )
        onView(withId(R.id.login_password_edit_text)).perform(
            typeText(VALID_PASSWORD),
            pressImeActionButton()
        )

        sleep(300)
        intended(hasComponent(MainActivity::class.java.name))
        Intents.release()
    }

    @Test
    @Ignore
    fun pressingTheCreateAccountLinkOpensPage() {
        // Couldn't find anything that says how this could be tested...
        // Leaving this here as it will need to be done at some point
    }
}