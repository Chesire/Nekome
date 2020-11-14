package com.chesire.nekome.app.login.syncing

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.app.login.R
import com.chesire.nekome.app.login.databinding.FragmentSyncingBinding
import com.chesire.nekome.core.nav.Flow
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to show a user that their series are currently being synced down.
 */
@LogLifecykle
@AndroidEntryPoint
class SyncingFragment : Fragment(R.layout.fragment_syncing) {

    private val viewModel by viewModels<SyncingViewModel>()
    private var _binding: FragmentSyncingBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding not set" }
    private lateinit var flow: Flow

    override fun onAttach(context: Context) {
        super.onAttach(context)
        flow = requireNotNull(context as? Flow) {
            "Hosting activity must implement ${Flow::class.java}"
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSyncingBinding.bind(view)

        observeAvatar()
        observeSyncStatus()
        viewModel.syncLatestData()
    }

    private fun observeAvatar() {
        viewModel.avatarUrl.observe(viewLifecycleOwner) {
            binding.profileImage.load(it) {
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_account_circle)
                error(R.drawable.ic_account_circle)
            }
        }
    }

    private fun observeSyncStatus() =
        viewModel.syncStatus.observe(viewLifecycleOwner) { flow.finishLogin() }
}
