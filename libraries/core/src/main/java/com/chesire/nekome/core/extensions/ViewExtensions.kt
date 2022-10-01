package com.chesire.nekome.core.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

/**
 * Hides the [View], animating its alpha to 0.0f.
 */
fun View.hide(invisible: Boolean = false) {
    animate().cancel()

    animate()
        .alpha(0.0f)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = if (invisible) View.INVISIBLE else View.GONE
            }
        })
        .start()
}

/**
 * Shows the [View], animating its alpha to 1.0f.
 */
fun View.show() {
    animate().cancel()

    visibility = View.VISIBLE
    animate()
        .alpha(1.0f)
        .setListener(null)
        .start()
}

/**
 * Makes the [View] visible if the [callback] condition is met.
 */
fun View.visibleIf(invisible: Boolean = false, animate: Boolean = false, callback: () -> Boolean) {
    if (callback()) {
        if (animate) show() else visibility = View.VISIBLE
    } else {
        if (animate) {
            hide(invisible)
        } else {
            visibility = if (invisible) View.INVISIBLE else View.GONE
        }
    }
}

/**
 * Animates the views alpha value from its current to [newAlpha].
 */
fun View.toAlpha(newAlpha: Float) {
    animate().cancel()
    animate()
        .alpha(newAlpha)
        .start()
}
