package com.chesire.nekome.app.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.app.discover.trending.TrendingAdapter
import com.chesire.nekome.core.flags.AsyncState
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to aid with Series discovery.
 */
@LogLifecykle
@AndroidEntryPoint
class DiscoverFragment : Fragment() {

    private val viewModel by viewModels<DiscoverViewModel>()
    private val animeTrendingAdapter = TrendingAdapter()
    private val mangaTrendingAdapter = TrendingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater
        .inflate(R.layout.fragment_discover, container, false)
        .apply {
            findViewById<RecyclerView>(R.id.discoverTrendingAnimeList).apply {
                adapter = animeTrendingAdapter
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                setHasFixedSize(true)
            }
            findViewById<RecyclerView>(R.id.discoverTrendingMangaList).apply {
                adapter = mangaTrendingAdapter
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                setHasFixedSize(true)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.trendingAnime.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AsyncState.Success -> animeTrendingAdapter.submitList(state.data)
                is AsyncState.Error -> {
                    // Show snackbar
                    // Show error view on trending anime
                }
                is AsyncState.Loading -> {
                    // Show loading view
                }
            }
        }
        viewModel.trendingManga.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AsyncState.Success -> mangaTrendingAdapter.submitList(state.data)
                is AsyncState.Error -> {
                    // Show snackbar
                    // Show error view on trending manga
                }
                is AsyncState.Loading -> {
                    // Show loading view
                }
            }
        }
    }
}
