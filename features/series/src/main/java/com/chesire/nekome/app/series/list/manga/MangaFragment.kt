package com.chesire.nekome.app.series.list.manga

import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.app.series.list.SeriesListFragment
import com.chesire.nekome.core.flags.SeriesType

/**
 * Fragment to display the list of all [SeriesType.Manga] series.
 */
@LogLifecykle
class MangaFragment : SeriesListFragment() {
    override val seriesType = SeriesType.Manga
}
