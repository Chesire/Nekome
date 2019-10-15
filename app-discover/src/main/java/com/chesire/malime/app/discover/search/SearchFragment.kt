package com.chesire.malime.app.discover.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.app.discover.R
import com.chesire.malime.core.flags.AsyncState
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.core.viewmodel.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_search.fragmentSearchRecyclerView
import timber.log.Timber
import javax.inject.Inject

@LogLifecykle
class SearchFragment : DaggerFragment(), SearchInteractionListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get<SearchViewModel>()
    }
    private lateinit var searchAdapter: SearchAdapter
    private val searchArgs: SearchFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchAdapter = SearchAdapter(this)
        fragmentSearchRecyclerView.apply {
            adapter = searchAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.searchResults.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is AsyncState.Success -> {
                        Timber.d("Search results has been updated, new count [${it.data.count()}]")
                        searchAdapter.submitList(it.data)
                    }
                    is AsyncState.Error -> Timber.w("Search failed: [${it.error}]")
                    is AsyncState.Loading -> Timber.v("Display load indicator")
                }
            }
        )

        viewModel.performSearch(searchArgs.seriesTitle)
    }

    override fun addSeries(model: SeriesModel) {
        Timber.i("Model ${model.slug} addSeries called")
        // Default to UserSeriesStatus.Current for now
        viewModel.addSeries(model, UserSeriesStatus.Current)
    }
}
