package com.chesire.malime.flow.series.list.anime

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.flow.series.list.SeriesListFragment

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

    override fun toDetails(model: SeriesModel, navigatorExtras: Pair<View, String>) =
        findNavController().navigate(
            AnimeFragmentDirections.toSeriesDetailFragment(model),
            FragmentNavigatorExtras(navigatorExtras)
        )

    override fun toSearch() =
        findNavController().navigate(AnimeFragmentDirections.toSearchFragment())
}
