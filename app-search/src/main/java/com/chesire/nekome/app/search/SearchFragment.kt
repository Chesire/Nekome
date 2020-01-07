package com.chesire.nekome.app.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.core.extensions.hide
import com.chesire.nekome.core.extensions.hideSystemKeyboard
import com.chesire.nekome.core.extensions.show
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_search.searchConfirmButton
import kotlinx.android.synthetic.main.fragment_search.searchLayout
import kotlinx.android.synthetic.main.fragment_search.searchProgress
import kotlinx.android.synthetic.main.fragment_search.searchSeriesLayout
import kotlinx.android.synthetic.main.fragment_search.searchSeriesText
import kotlinx.android.synthetic.main.fragment_search.seriesDetailStatusGroup
import timber.log.Timber
import javax.inject.Inject

/**
 * Allows a user to perform a search to find new series to follow.
 */
@LogLifecykle
class SearchFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<SearchViewModel> { viewModelFactory }
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
        searchSeriesText.addTextChangedListener {
            searchSeriesLayout.error = null
        }
        observeSearchResults()
    }

    private fun submitSearch() {
        activity?.hideSystemKeyboard()
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
                    is AsyncState.Success -> {
                        hideSpinner()
                        findNavController().navigate(
                            SearchFragmentDirections.toResultsFragment(
                                result.data.toTypedArray()
                            )
                        )
                    }
                    is AsyncState.Error -> {
                        hideSpinner()
                        parseSearchError(result.error)
                    }
                    is AsyncState.Loading -> showSpinner()
                }
            }
        )
    }

    private fun parseSearchError(error: SearchError) {
        when (error) {
            SearchError.EmptyTitle ->
                searchSeriesLayout.error = getString(R.string.search_error_no_text)
            SearchError.GenericError -> Snackbar.make(
                searchLayout,
                R.string.error_generic,
                Snackbar.LENGTH_LONG
            ).show()
            SearchError.NoTypeSelected -> Snackbar.make(
                searchLayout,
                R.string.search_error_no_type_selected,
                Snackbar.LENGTH_LONG
            ).show()
            SearchError.NoSeriesFound -> Snackbar.make(
                searchLayout,
                R.string.search_error_no_series_found,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun showSpinner() {
        Timber.d("Showing Spinner")
        searchConfirmButton.text = ""
        searchConfirmButton.isClickable = false
        searchProgress.show()
    }

    private fun hideSpinner() {
        Timber.d("Hiding Spinner")
        searchConfirmButton.text = getString(R.string.search_search)
        searchConfirmButton.isClickable = true
        searchProgress.hide(invisible = true)
    }
}
