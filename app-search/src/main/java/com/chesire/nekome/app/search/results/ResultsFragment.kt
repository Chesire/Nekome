package com.chesire.nekome.app.search.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.app.search.R
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.core.viewmodel.ViewModelFactory
import com.chesire.nekome.server.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_results.resultsLayout
import kotlinx.android.synthetic.main.fragment_results.resultsRecyclerView
import javax.inject.Inject

/**
 * Displays the results of a search to the user, allowing them to select to track new series.
 */
@LogLifecykle
class ResultsFragment : DaggerFragment(), ResultsListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<ResultsViewModel> { viewModelFactory }
    private val args by navArgs<ResultsFragmentArgs>()
    private val resultsAdapter = ResultsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_results, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeSeries()
        resultsAdapter.submitList(args.searchResults.toList())
        resultsRecyclerView.apply {
            adapter = resultsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun observeSeries() {
        viewModel.series.observe(viewLifecycleOwner, Observer {
            resultsAdapter.allSeries = it
        })
    }

    override fun onTrack(model: SeriesModel, callback: () -> Unit) {
        viewModel.trackNewSeries(model) {
            if (it is Resource.Error) {
                Snackbar.make(
                    resultsLayout,
                    getString(R.string.results_failure, model.title),
                    Snackbar.LENGTH_LONG
                ).show()
            }
            callback()
        }
    }
}
