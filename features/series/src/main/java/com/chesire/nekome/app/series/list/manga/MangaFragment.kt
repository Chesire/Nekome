package com.chesire.nekome.app.series.list.manga

import com.chesire.nekome.app.series.list.SeriesListFragment
import com.chesire.nekome.core.flags.SeriesType
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to display the list of all [SeriesType.Manga] series.
 */
@AndroidEntryPoint
class MangaFragment : SeriesListFragment() {
    override val seriesType = SeriesType.Manga
}
