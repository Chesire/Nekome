package com.chesire.malime.flow.login.syncing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.AsyncState
import com.chesire.malime.databinding.FragmentSyncingBinding
import com.chesire.malime.flow.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_syncing.fragmentSyncingProgress
import kotlinx.android.synthetic.main.fragment_syncing.fragmentSyncingRetryButton
import javax.inject.Inject

@LogLifecykle
class SyncingFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(SyncingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSyncingBinding
            .inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                fragmentSyncingRetryButton.setOnClickListener { viewModel.syncLatestData() }
            }
            .root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.syncStatus.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is AsyncState.Success -> {
                        findNavController().navigate(
                            SyncingFragmentDirections.actionSyncingFragmentToOverviewActivity()
                        )
                        activity?.finish()
                    }
                    is AsyncState.Loading -> {
                        fragmentSyncingProgress.visibility = View.VISIBLE
                        fragmentSyncingRetryButton.visibility = View.GONE
                    }
                    is AsyncState.Error -> {
                        fragmentSyncingProgress.visibility = View.GONE
                        fragmentSyncingRetryButton.visibility = View.VISIBLE
                    }
                }
            }
        )
        viewModel.syncLatestData()
    }
}
