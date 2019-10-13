package com.chesire.malime.app.series.list.manga

import android.os.Bundle
import androidx.lifecycle.Observer
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.app.series.list.SeriesListFragment

@LogLifecykle
class MangaFragment : SeriesListFragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.mangaSeries.observe(
            viewLifecycleOwner,
            Observer {
                newSeriesListProvided(it)
            }
        )
    }
}
