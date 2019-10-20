package com.chesire.malime.app.search.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.app.search.R
import com.chesire.malime.core.viewmodel.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_results.resultsRecyclerView
import javax.inject.Inject

/**
 * Displays the results of a search to the user, allowing them to select to track new series.
 */
@LogLifecykle
class ResultsFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get<ResultsViewModel>()
    }
    private val args by navArgs<ResultsFragmentArgs>()
    private val resultsAdapter = ResultsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_results, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resultsAdapter.submitList(args.searchResults.toList())
        resultsRecyclerView.apply {
            adapter = resultsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }
}
