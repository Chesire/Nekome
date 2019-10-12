package com.chesire.malime.core.extensions

import androidx.fragment.app.Fragment

/**
 * Get the [key] from the bundle, requiring that the result is not null.
 */
inline fun <reified T : Any> Fragment.extraNotNull(key: String, default: T? = null) = lazy {
    val value = arguments?.get(key)
    requireNotNull(if (value is T) value else default) { key }
}
