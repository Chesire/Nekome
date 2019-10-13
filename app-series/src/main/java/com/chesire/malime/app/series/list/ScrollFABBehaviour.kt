package com.chesire.malime.app.series.list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val SCROLL_THRESHOLD = 4

/**
 * Provides scrolling for a [FloatingActionButton] by attaching to a [RecyclerView] it depends on.
 */
@Suppress("UnusedPrivateMember")
class ScrollFABBehaviour(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior() {
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: FloatingActionButton,
        dependency: View
    ): Boolean {
        return (dependency as? RecyclerView)?.let {
            dependency.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val forceVisible = (recyclerView.layoutManager as? LinearLayoutManager)
                        ?.findFirstCompletelyVisibleItemPosition() == 0
                    if (child.isShown && dy > SCROLL_THRESHOLD) {
                        child.hide()
                    } else if (!child.isShown && (dy < -SCROLL_THRESHOLD || forceVisible)) {
                        child.show()
                    }
                }
            })
            true
        } ?: false
    }
}
