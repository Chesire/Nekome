package com.chesire.malime.app.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.chesire.malime.core.flags.AsyncState
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.viewmodel.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_search.searchConfirmButton
import kotlinx.android.synthetic.main.fragment_search.searchSeriesText
import kotlinx.android.synthetic.main.fragment_search.seriesDetailStatusGroup
import javax.inject.Inject

/**
 * Allows a user to perform a search to find new series to follow.
 */
class SearchFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get<SearchViewModel>()
    }
    private val seriesType: SeriesType
        get() = when (seriesDetailStatusGroup.checkedChipId) {
            R.id.searchChipAnime -> SeriesType.Anime
            R.id.searchChipManga -> SeriesType.Manga
            else -> SeriesType.Unknown
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchConfirmButton.setOnClickListener { submitSearch() }
        observeSearchResults()
    }

    private fun submitSearch() {
        viewModel.executeSearch(
            SearchData(
                searchSeriesText.text.toString(),
                seriesType
            )
        )
    }

    private fun observeSearchResults() {
        viewModel.searchResult.observe(
            viewLifecycleOwner,
            Observer { result ->
                when (result) {
                    is AsyncState.Success -> findNavController().navigate(
                        SearchFragmentDirections.toResultsFragment(
                            result.data.toTypedArray()
                        )
                    )
                    is AsyncState.Error -> {
                        // check error and choose what to do based on it
                    }
                    is AsyncState.Loading -> {
                        // show searching indicator
                    }
                }
            }
        )
    }
}
