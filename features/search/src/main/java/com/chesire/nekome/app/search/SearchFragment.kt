package com.chesire.nekome.app.search

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.app.search.databinding.FragmentSearchBinding
import com.chesire.nekome.core.extensions.hide
import com.chesire.nekome.core.extensions.hideSystemKeyboard
import com.chesire.nekome.core.extensions.show
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.core.flags.SeriesType
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * Allows a user to perform a search to find new series to follow.
 */
@LogLifecykle
@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel by viewModels<SearchViewModel>()

    @Inject
    lateinit var searchPreferences: SearchPreferences
    private val seriesType: SeriesType
        get() = when (binding.searchChipGroup.checkedChipId) {
            R.id.searchChipAnime -> SeriesType.Anime
            R.id.searchChipManga -> SeriesType.Manga
            else -> SeriesType.Unknown
        }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding not set" }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        setInitialSeriesType()
        binding.searchConfirmButton.setOnClickListener { submitSearch() }
        binding.searchText.addTextChangedListener {
            binding.searchTextLayout.error = null
        }
        binding.searchChipGroup.setOnCheckedChangeListener { _, checkedId ->
            val type = when (checkedId) {
                R.id.searchChipAnime -> SeriesType.Anime
                R.id.searchChipManga -> SeriesType.Manga
                else -> SeriesType.Unknown
            }
            searchPreferences.lastSearchType = type.id
        }
        observeSearchResults()
    }

    private fun setInitialSeriesType() {
        val chipId = when (SeriesType.forId(searchPreferences.lastSearchType)) {
            SeriesType.Manga -> R.id.searchChipManga
            else -> R.id.searchChipAnime
        }
        binding.searchChipGroup.check(chipId)
    }

    private fun submitSearch() {
        activity?.hideSystemKeyboard()
        viewModel.executeSearch(
            SearchData(binding.searchText.text.toString(), seriesType)
        )
    }

    private fun observeSearchResults() {
        viewModel.searchResult.observe(viewLifecycleOwner) { result ->
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
    }

    private fun parseSearchError(error: SearchError) {
        when (error) {
            SearchError.EmptyTitle ->
                binding.searchTextLayout.error = getString(R.string.search_error_no_text)
            SearchError.GenericError -> Snackbar.make(
                binding.searchLayout,
                R.string.error_generic,
                Snackbar.LENGTH_LONG
            ).show()
            SearchError.NoTypeSelected -> Snackbar.make(
                binding.searchLayout,
                R.string.search_error_no_type_selected,
                Snackbar.LENGTH_LONG
            ).show()
            SearchError.NoSeriesFound -> Snackbar.make(
                binding.searchLayout,
                R.string.search_error_no_series_found,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun showSpinner() {
        Timber.d("Showing Spinner")
        binding.searchConfirmButton.text = ""
        binding.searchConfirmButton.isClickable = false
        binding.searchProgress.show()
    }

    private fun hideSpinner() {
        Timber.d("Hiding Spinner")
        binding.searchConfirmButton.text = getString(R.string.search_search)
        binding.searchConfirmButton.isClickable = true
        binding.searchProgress.hide(invisible = true)
    }
}
