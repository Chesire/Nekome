package com.chesire.nekome.core.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

/**
 * Hides the system keyboard.
 */
fun Activity.hideSystemKeyboard() {
    currentFocus?.let {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}
