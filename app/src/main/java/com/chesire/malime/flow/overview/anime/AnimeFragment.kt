package com.chesire.malime.flow.overview.anime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.chesire.malime.databinding.FragmentAnimeBinding
import com.chesire.malime.flow.ViewModelFactory
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

class AnimeFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(AnimeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentAnimeBinding.inflate(inflater, container, false).root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.animeSeries.observe(
            viewLifecycleOwner,
            Observer {
                val s = it
                Timber.d("New series found")
            }
        )

        viewModel.refresh()
    }

    companion object {
        const val TAG = "AnimeFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AnimeFragment.
         */
        fun newInstance() = AnimeFragment()
    }
}
