package com.chesire.malime.app.series.list.anime

import android.os.Bundle
import androidx.lifecycle.Observer
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.app.series.list.SeriesListFragment
import com.chesire.malime.core.flags.SeriesType

/**
 * Fragment to display the list of all [SeriesType.Anime] series.
 */
@LogLifecykle
class AnimeFragment : SeriesListFragment() {
    override val seriesType = SeriesType.Anime
}
