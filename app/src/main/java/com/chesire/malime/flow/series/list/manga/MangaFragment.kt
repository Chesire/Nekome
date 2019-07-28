package com.chesire.malime.flow.series.list.manga

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.flow.series.list.SeriesListFragment

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

    override fun toDetails(model: SeriesModel, navigatorExtras: Pair<View, String>) =
        findNavController().navigate(
            MangaFragmentDirections.toSeriesDetailFragment(model),
            FragmentNavigatorExtras(navigatorExtras)
        )

    override fun toSearch() =
        findNavController().navigate(MangaFragmentDirections.toSearchFragment())
}
