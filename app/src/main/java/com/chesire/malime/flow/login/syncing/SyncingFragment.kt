package com.chesire.malime.flow.login.syncing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R
import com.chesire.malime.core.viewmodel.ViewModelFactory
import com.chesire.malime.databinding.FragmentSyncingBinding
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_syncing.syncingProfileImage
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
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeAvatar()
        observeSyncStatus()
        viewModel.syncLatestData()
    }

    private fun observeAvatar() {
        viewModel.avatarUrl.observe(
            viewLifecycleOwner,
            Observer {
                Glide.with(this)
                    .load(it)
                    .placeholder(R.drawable.ic_account_circle)
                    .circleCrop()
                    .into(syncingProfileImage)
            }
        )
    }

    private fun observeSyncStatus() {
        viewModel.syncStatus.observe(
            viewLifecycleOwner,
            Observer {
                findNavController().navigate(SyncingFragmentDirections.toAnimeFragment())
            }
        )
    }
}
