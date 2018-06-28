package com.chesire.malime.view.maldisplay

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

/**
 * A recycler view with a state for being empty.
 *
 * based on: https://gist.github.com/adelnizamutdinov/31c8f054d1af4588dc5c
 */
class EmptyableRecyclerView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    private lateinit var emptyView: View
    private val observer: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            checkIfEmpty()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter != null && adapter.hasObservers()) {
            adapter.unregisterAdapterDataObserver(observer)
        }

        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(observer)

        checkIfEmpty()
    }

    fun checkIfEmpty() {
        if (adapter != null) {
            val emptyViewVisible = adapter.itemCount == 0
            emptyView.visibility = if (emptyViewVisible) VISIBLE else GONE
            visibility = if (emptyViewVisible) GONE else VISIBLE
        }
    }

    fun setEmptyView(newEmptyView: View) {
        emptyView = newEmptyView
        checkIfEmpty()
    }
}
