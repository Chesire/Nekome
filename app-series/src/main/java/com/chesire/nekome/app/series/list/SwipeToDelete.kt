package com.chesire.nekome.app.series.list

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

private const val FADE_BUFFER = 90

/**
 * [ItemTouchHelper] that fades a view out to allow deletion.
 */
class SwipeToDelete(
    private val adapter: SeriesAdapter
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false // No implementation required

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) =
        adapter.attemptDeleteItem(viewHolder.adapterPosition)

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val maxWidth = recyclerView.width
        val alphaValue = when {
            dX > 0 -> FADE_BUFFER - dX / maxWidth * 100
            dX < 0 -> FADE_BUFFER + dX / maxWidth * 100
            else -> 100.toFloat()
        }
        viewHolder.itemView.alpha = alphaValue / 100
    }
}
