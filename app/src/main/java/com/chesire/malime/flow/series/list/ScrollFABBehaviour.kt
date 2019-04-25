package com.chesire.malime.flow.series.list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Provides scrolling for a [FloatingActionButton] by attaching to a [RecyclerView] it depends on.
 */
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

                    if (dy > 0 && child.isShown) {
                        child.hide()
                    } else if (dy < 0 && !child.isShown) {
                        child.show()
                    }
                }
            })
            true
        } ?: false
    }
}
