package com.chesire.nekome.core.extensions

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes

/**
 * Sets text to be linked, along with this it will bold the text.
 *
 * [defaultString] should be the initial string to be modified, [replaceTarget] the part of the
 * string that should be found and modified to be a link, and [action] is the callback to execute
 * when the link is pressed.
 */
fun TextView.setLinkedText(
    @StringRes defaultString: Int,
    @StringRes replaceTarget: Int,
    action: () -> Unit
) {
    val spannedString = SpannableString(context.getString(defaultString))
    val target = context.getString(replaceTarget)
    val start = spannedString.indexOf(target)

    spannedString.apply {
        setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            start + target.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        setSpan(
            object : ClickableSpan() {
                override fun onClick(p0: View) {
                    action()
                }
            },
            start,
            start + target.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
    }
    text = spannedString
    movementMethod = LinkMovementMethod.getInstance()
    setLinkTextColor(currentTextColor)
}
