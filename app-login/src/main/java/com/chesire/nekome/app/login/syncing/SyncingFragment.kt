package com.chesire.nekome.app.login.syncing

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.api.load
import coil.transform.CircleCropTransformation
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.app.login.R
import com.chesire.nekome.app.login.databinding.FragmentSyncingBinding
import com.chesire.nekome.core.nav.Flow
import com.chesire.nekome.core.viewmodel.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * Fragment to show a user that their series are currently being synced down.
 */
@LogLifecykle
class SyncingFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<SyncingViewModel> { viewModelFactory }
    private var _binding: FragmentSyncingBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding not set" }
    private lateinit var flow: Flow

    override fun onAttach(context: Context) {
        super.onAttach(context)
        flow = requireNotNull(context as? Flow) {
            "Hosting activity must implement ${Flow::class.java}"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSyncingBinding.inflate(inflater, container, false).also { _binding = it }.root

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
                binding.profileImage.load(it) {
                    transformations(CircleCropTransformation())
                    placeholder(R.drawable.ic_account_circle)
                    error(R.drawable.ic_account_circle)
                }
            }
        )
    }

    private fun observeSyncStatus() {
        viewModel.syncStatus.observe(
            viewLifecycleOwner,
            Observer {
                flow.finishLogin()
            }
        )
    }
}
