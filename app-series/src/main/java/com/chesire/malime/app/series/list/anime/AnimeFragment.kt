package com.chesire.malime.app.series.list.anime

import android.os.Bundle
import androidx.lifecycle.Observer
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.app.series.list.SeriesListFragment
import com.chesire.malime.core.flags.SeriesType

@LogLifecykle
class AnimeFragment : SeriesListFragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.series.observe(
            viewLifecycleOwner,
            Observer {
                newSeriesListProvided(it.filter { it.type == SeriesType.Anime })
            }
        )
    }
}
