package com.chesire.malime.flow.series.list.anime

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.flow.series.list.SeriesListFragment
import timber.log.Timber

@LogLifecykle
class AnimeFragment : SeriesListFragment() {
    private val viewModel by lazy {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(AnimeViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.animeSeries.observe(
            viewLifecycleOwner,
            Observer {
                newSeriesListProvided(it)
            }
        )
    }

    override fun onPlusOne(model: SeriesModel) {
        Timber.i("Model ${model.slug} onPlusOne called")
        viewModel.updateSeries(model.userId, model.progress.inc(), model.userSeriesStatus)
    }

    override fun toDetails(model: SeriesModel, navigatorExtras: Pair<View, String>) =
        findNavController().navigate(
            AnimeFragmentDirections.toSeriesDetailFragment(model),
            FragmentNavigatorExtras(navigatorExtras)
        )

    override fun toSearch() =
        findNavController().navigate(AnimeFragmentDirections.toSearchFragment())
}
