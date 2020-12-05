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
import com.chesire.nekome.app.search.domain.SearchModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Displays the results of a search to the user, allowing them to select to track new series.
 */
@LogLifecykle
@AndroidEntryPoint
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

    private fun observeSeries() = viewModel.seriesIds.observe(viewLifecycleOwner) { series ->
        resultsAdapter.storedSeriesIds = series
    }

    override fun onTrack(model: SearchModel, callback: () -> Unit) {
        viewModel.trackNewSeries(
            ResultsData(model.id, model.type)
        ) { successful ->
            if (!successful) {
                Snackbar.make(
                    binding.resultsLayout,
                    getString(R.string.results_failure, model.canonicalTitle),
                    Snackbar.LENGTH_LONG
                ).show()
            }

            callback()
        }
    }
}
