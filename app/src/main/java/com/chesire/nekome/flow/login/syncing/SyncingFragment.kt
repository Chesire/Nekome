package com.chesire.nekome.flow.login.syncing

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
import com.chesire.nekome.R
import com.chesire.nekome.core.viewmodel.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_syncing.syncingProfileImage
import javax.inject.Inject

/**
 * Fragment to show a user that their series are currently being synced down.
 */
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
    ): View? = inflater.inflate(R.layout.fragment_syncing, container, false)

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
