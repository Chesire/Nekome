package com.chesire.nekome.core.url

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import javax.inject.Inject

/**
 * A [UrlHandler] that launches a URL in a [CustomTabsIntent].
 */
class CustomTabsUrl @Inject constructor() : UrlHandler {
    override fun launch(context: Context, url: String) {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(
                context,
                Uri.parse(url)
            )
    }
}
