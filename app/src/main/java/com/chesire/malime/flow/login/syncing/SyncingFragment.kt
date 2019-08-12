package com.chesire.malime.flow.login.syncing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.AsyncState
import com.chesire.malime.databinding.FragmentSyncingBinding
import com.chesire.malime.extensions.hide
import com.chesire.malime.extensions.show
import com.chesire.malime.flow.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_syncing.fragmentSyncingAnimation
import kotlinx.android.synthetic.main.fragment_syncing.fragmentSyncingRetryButton
import javax.inject.Inject

@LogLifecykle
class SyncingFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get<SyncingViewModel>()
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
                    is AsyncState.Success -> findNavController().navigate(SyncingFragmentDirections.toAnimeFragment())
                    is AsyncState.Loading -> setLoading()
                    is AsyncState.Error -> hideLoading()
                }
            }
        )
        viewModel.syncLatestData()
    }

    private fun setLoading() {
        fragmentSyncingAnimation.show()
        fragmentSyncingRetryButton.hide()
    }

    private fun hideLoading() {
        fragmentSyncingAnimation.hide()
        fragmentSyncingRetryButton.show()
    }
}
