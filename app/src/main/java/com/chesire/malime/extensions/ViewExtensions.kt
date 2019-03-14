package com.chesire.malime.extensions

import android.view.View

fun View.visibleIf(invisible: Boolean = false, callback: () -> Boolean) {
    visibility = if (callback()) {
        View.VISIBLE
    } else {
        if (invisible) View.INVISIBLE else View.GONE
    }
}
