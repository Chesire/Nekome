package com.chesire.malime.util

import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.ViewCompat
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
) : androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior<V>(context, attrs) {
    override fun onStartNestedScroll(
        coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean = axes == ViewCompat.SCROLL_AXIS_VERTICAL

    override fun layoutDependsOn(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: V, dependency: View): Boolean {
        if (dependency is Snackbar.SnackbarLayout) {
            updateSnackbar(child, dependency)
        }
        return super.layoutDependsOn(parent, child, dependency)
    }

    override fun onNestedPreScroll(
        coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout,
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
        (snackbarLayout.layoutParams as? androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams)?.apply {
            anchorId = child.id
            anchorGravity = Gravity.TOP
            gravity = Gravity.TOP
            snackbarLayout.layoutParams = this
        }
    }
}
