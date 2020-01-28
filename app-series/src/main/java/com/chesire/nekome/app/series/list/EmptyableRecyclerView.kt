package com.chesire.nekome.app.series.list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chesire.nekome.core.extensions.visibleIf

/**
 * [RecyclerView] that shows [emptyView] if the adapter is empty.
 */
class EmptyableRecyclerView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val adapterObserver = object : AdapterDataObserver() {
        override fun onChanged() = checkIfEmpty()

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = checkIfEmpty()

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = checkIfEmpty()
    }

    /**
     * Sets the [View] that should be used when the adapter is empty.
     */
    var emptyView: View? = null
        set(newView) {
            field = newView
            checkIfEmpty()
        }

    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter != null && adapter.hasObservers()) {
            adapter.unregisterAdapterDataObserver(adapterObserver)
        }

        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(adapterObserver)

        checkIfEmpty()
    }

    private fun checkIfEmpty() {
        adapter?.let { adap ->
            emptyView?.let { empty ->
                val emptyViewVisible = adap.itemCount == 0
                empty.visibleIf { emptyViewVisible }
                visibleIf { !emptyViewVisible }
            }
        }
    }
}
