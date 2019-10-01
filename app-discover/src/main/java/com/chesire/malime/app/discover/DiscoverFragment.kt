package com.chesire.malime.app.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.core.flags.AsyncState
import com.chesire.malime.flow.ViewModelFactory
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_discover, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.trendingAnime.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is AsyncState.Success -> {
                    // Populate the list
                }
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
                is AsyncState.Success -> {
                    // Populate the list
                }
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
}
