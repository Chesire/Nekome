package com.chesire.nekome.app.series.list.anime

import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.app.series.list.SeriesListFragment
import com.chesire.nekome.core.flags.SeriesType

/**
 * Fragment to display the list of all [SeriesType.Anime] series.
 */
@LogLifecykle
class AnimeFragment : SeriesListFragment() {
    override val seriesType = SeriesType.Anime
}
