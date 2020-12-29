package com.chesire.nekome.app.series.list.anime

import com.chesire.nekome.app.series.list.SeriesListFragment
import com.chesire.nekome.dataflags.SeriesType
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to display the list of all [SeriesType.Anime] series.
 */
@AndroidEntryPoint
class AnimeFragment : SeriesListFragment() {
    override val seriesType = SeriesType.Anime
}
