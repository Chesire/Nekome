package com.chesire.malime.app.series.list.manga

import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.app.series.list.SeriesListFragment
import com.chesire.malime.core.flags.SeriesType

/**
 * Fragment to display the list of all [SeriesType.Manga] series.
 */
@LogLifecykle
class MangaFragment : SeriesListFragment() {
    override val seriesType = SeriesType.Manga
}
