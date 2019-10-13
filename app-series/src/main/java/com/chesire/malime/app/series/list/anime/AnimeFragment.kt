package com.chesire.malime.app.series.list.anime

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.app.series.list.SeriesListFragment

@LogLifecykle
class AnimeFragment : SeriesListFragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.animeSeries.observe(
            viewLifecycleOwner,
            Observer {
                newSeriesListProvided(it)
            }
        )
    }

    override fun toSearch() =
        findNavController().navigate(AnimeFragmentDirections.toSearchFragment())
}
