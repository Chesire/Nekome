package com.chesire.malime.util

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import com.chesire.malime.R
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.models.MalimeModel
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

class UrlLoader @Inject constructor() {
    fun loadProfile(context: Context, service: SupportedService, userId: Int) {
        val profileUrl = when (service) {
            SupportedService.Kitsu -> String.format(
                Locale.ROOT,
                context.getString(R.string.view_profile_kitsu),
                userId
            )
            SupportedService.MyAnimeList -> String.format(
                Locale.ROOT,
                context.getString(R.string.view_profile_mal),
                userId
            )
            else -> ""
        }

        if (profileUrl.isEmpty()) {
            Timber.w("Unknown service passed for the loadProfile, not attempting to load url")
            return
        }

        loadUrl(context, Uri.parse(profileUrl))
    }

    fun loadSeries(context: Context, service: SupportedService, item: MalimeModel) {
        val seriesUrl = when (service) {
            SupportedService.Kitsu -> String.format(
                Locale.ROOT,
                context.getString(R.string.view_series_kitsu),
                item.type.text,
                item.slug
            )
            SupportedService.MyAnimeList -> String.format(
                Locale.ROOT,
                context.getString(R.string.view_series_mal),
                item.type.text,
                item.seriesId
            )
            else -> ""
        }

        if (seriesUrl.isEmpty()) {
            Timber.w("Unknown service passed for the loadSeries, not attempting to load url")
            return
        }

        loadUrl(context, Uri.parse(seriesUrl))
    }

    private fun loadUrl(context: Context, uri: Uri) {
        CustomTabsIntent.Builder().build().launchUrl(context, uri)
    }
}
