package com.chesire.malime.util

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import kotlin.math.max
import kotlin.math.min

/**
 * Provides behavior to make the bottom navigation scroll down when the frame above it scrolls.
 */
@Suppress("UNUSED")
class BottomNavigationBehavior<V : View>(
    context: Context,
    attrs: AttributeSet
) : CoordinatorLayout.Behavior<V>(context, attrs) {
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean = axes == ViewCompat.SCROLL_AXIS_VERTICAL

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        if (dependency is Snackbar.SnackbarLayout) {
            updateSnackbar(child, dependency)
        }
        return super.layoutDependsOn(parent, child, dependency)
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        child.translationY = max(0f, min(child.height.toFloat(), child.translationY + dy))
    }

    private fun updateSnackbar(child: View, snackbarLayout: Snackbar.SnackbarLayout) {
        (snackbarLayout.layoutParams as? CoordinatorLayout.LayoutParams)?.apply {
            anchorId = child.id
            anchorGravity = Gravity.TOP
            gravity = Gravity.TOP
            snackbarLayout.layoutParams = this
        }
    }
}