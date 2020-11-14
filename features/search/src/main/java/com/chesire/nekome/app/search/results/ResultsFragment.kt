package com.chesire.nekome.app.search.results

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.app.search.R
import com.chesire.nekome.app.search.databinding.FragmentResultsBinding
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.server.Resource
import com.google.android.material.snackbar.Snackbar

/**
 * Displays the results of a search to the user, allowing them to select to track new series.
 */
@LogLifecykle
class ResultsFragment : Fragment(R.layout.fragment_results), ResultsListener {

    private val viewModel by viewModels<ResultsViewModel>()
    private val args by navArgs<ResultsFragmentArgs>()
    private val resultsAdapter = ResultsAdapter(this)
    private var _binding: FragmentResultsBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding not set" }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentResultsBinding.bind(view)

        observeSeries()
        resultsAdapter.submitList(args.searchResults.toList())
        binding.resultsContent.apply {
            adapter = resultsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun observeSeries() = viewModel.series.observe(viewLifecycleOwner) { series ->
        resultsAdapter.allSeries = series
    }

    override fun onTrack(model: SeriesModel, callback: () -> Unit) {
        viewModel.trackNewSeries(model) {
            if (it is Resource.Error) {
                Snackbar.make(
                    binding.resultsLayout,
                    getString(R.string.results_failure, model.title),
                    Snackbar.LENGTH_LONG
                ).show()
            }
            callback()
        }
    }
}
