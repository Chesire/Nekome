package com.chesire.malime.extensions

import android.animation.Animator
import android.view.View

/**
 * Hides the [View], animating its alpha to 0.0f.
 */
fun View.hide(invisible: Boolean = false) {
    animate()
        .alpha(0.0f)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                visibility = if (invisible) View.INVISIBLE else View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}
        })
}

/**
 * Shows the [View], animating its alpha to 1.0f.
 */
fun View.show() {
    visibility = View.VISIBLE
    animate()
        .alpha(1.0f)
        .setListener(null)
}

/**
 * Makes the [View] visible if the [callback] condition is met.
 */
fun View.visibleIf(invisible: Boolean = false, callback: () -> Boolean) {
    visibility = if (callback()) {
        View.VISIBLE
    } else {
        if (invisible) View.INVISIBLE else View.GONE
    }
}
