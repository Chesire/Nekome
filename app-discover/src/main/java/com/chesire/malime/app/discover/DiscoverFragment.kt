package com.chesire.malime.app.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.app.discover.trending.TrendingAdapter
import com.chesire.malime.core.flags.AsyncState
import com.chesire.malime.core.viewmodel.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * Fragment to aid with Series discovery.
 */
@LogLifecykle
class DiscoverFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get<DiscoverViewModel>()
    }
    private val animeTrendingAdapter = TrendingAdapter()
    private val mangaTrendingAdapter = TrendingAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

        viewModel.trendingAnime.observe(viewLifecycleOwner, Observer { state ->
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
        })
        viewModel.trendingManga.observe(viewLifecycleOwner, Observer { state ->
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
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_discover, menu)

        val actionMenuItem = menu.findItem(R.id.menuDiscoverSearch)
        val actionView = actionMenuItem.actionView as? SearchView
        actionView?.queryHint = getString(R.string.discover_search_hint)
        actionView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!actionView.isIconified) {
                    actionView.isIconified = true
                }
                actionMenuItem.collapseActionView()

                if (!query.isNullOrBlank()) {
                    navigateToSearch(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?) = false
        })
    }

    private fun navigateToSearch(title: String) {
        // perform navigation to search, passing in the title to search for
    }
}
