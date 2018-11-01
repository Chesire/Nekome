package com.chesire.malime.tools

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.matcher.ViewMatchers.withText
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import android.view.WindowManager.LayoutParams.TYPE_TOAST
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * This class allows to match Toast messages in tests with Espresso.
 *
 * Idea taken from: https://stackoverflow.com/a/33387980
 *
 * Usage in test class:
 *
 * // To assert a toast does *not* pop up:
 * onToast("text").check(doesNotExist())
 * onToast(textId).check(doesNotExist())
 *
 * // To assert a toast does pop up:
 * onToast("text").check(matches(isDisplayed()))
 * onToast(textId).check(matches(isDisplayed()))
 */
class ToastMatcher(private val maxFailures: Int = DEFAULT_MAX_FAILURES) : TypeSafeMatcher<Root>() {

    /** Restrict number of false results from matchesSafely to avoid endless loop */
    private var failures = 0

    override fun describeTo(description: Description) {
        description.appendText("is toast")
    }

    public override fun matchesSafely(root: Root): Boolean {
        val type = root.windowLayoutParams.get().type
        @Suppress("DEPRECATION") // TYPE_TOAST is deprecated in favor of TYPE_APPLICATION_OVERLAY
        if (type == TYPE_TOAST || type == TYPE_APPLICATION_OVERLAY) {
            val windowToken = root.decorView.windowToken
            val appToken = root.decorView.applicationWindowToken
            if (windowToken === appToken) {
                // windowToken == appToken means this window isn't contained by any other windows.
                // if it was a window for an activity, it would have TYPE_BASE_APPLICATION.
                return true
            }
        }
        // Method is called again if false is returned which is useful because a toast may take some time to pop up. But for
        // obvious reasons an infinite wait isn't of help. So false is only returned as often as maxFailures specifies.
        return (++failures >= maxFailures)
    }

    companion object {
        /** Default for maximum number of retries to wait for the toast to pop up */
        private const val DEFAULT_MAX_FAILURES = 5

        fun onToast(text: String, maxRetries: Int = DEFAULT_MAX_FAILURES) =
            onView(withText(text)).inRoot(
                isToast(
                    maxRetries
                )
            )!!

        fun onToast(textId: Int, maxRetries: Int = DEFAULT_MAX_FAILURES) =
            onView(withText(textId)).inRoot(
                isToast(
                    maxRetries
                )
            )!!

        fun isToast(maxRetries: Int = DEFAULT_MAX_FAILURES): Matcher<Root> {
            return ToastMatcher(maxRetries)
        }
    }
}
