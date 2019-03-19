package com.chesire.malime.flow.series.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.chesire.malime.databinding.FragmentSearchBinding
import com.chesire.malime.flow.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SearchFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSearchBinding.inflate(inflater, container, false)
            .apply {
                vm = viewModel
                lifecycleOwner = viewLifecycleOwner
            }
            .root
    }

    companion object {
        const val TAG = "SearchFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SearchFragment.
         */
        fun newInstance() = SearchFragment()
    }
}
