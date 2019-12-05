package com.chesire.nekome.helpers

import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher
import org.hamcrest.Matchers

/**
 * Performs a click on the span represented by [textId] in a spanned string.
 */
fun clickClickableSpan(textId: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> = Matchers.instanceOf(TextView::class.java)

        override fun getDescription() = "clicking on clickable span with text id: $textId"

        override fun perform(uiController: UiController?, view: View?) {
            if (view !is TextView || view.text !is SpannableString || view.text.isEmpty()) {
                throw NoMatchingViewException.Builder()
                    .includeViewHierarchy(true)
                    .withRootView(view)
                    .build()
            }

            val text = textId.getResource()
            val spannable = view.text as SpannableString
            spannable.getSpans(0, spannable.length, ClickableSpan::class.java).forEach {
                val start = spannable.getSpanStart(it)
                val end = spannable.getSpanEnd(it)
                val sequence = spannable.subSequence(start, end)
                if (text == sequence.toString()) {
                    it.onClick(view)
                    return
                }
            }
            throw NoMatchingViewException.Builder()
                .includeViewHierarchy(true)
                .withRootView(view)
                .build()
        }
    }
}
