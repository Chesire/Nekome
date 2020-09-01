package com.chesire.nekome.core.url

import android.content.Context

/**
 * Specifies a class is able to handle URLs and launching them.
 */
interface UrlHandler {
    /**
     * Launch the provided [url].
     */
    fun launch(context: Context, url: String)
}
