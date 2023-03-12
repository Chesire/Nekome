package com.chesire.nekome.app.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chesire.nekome.app.discover.trending.TrendingAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to aid with Series discovery.
 */
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
}
