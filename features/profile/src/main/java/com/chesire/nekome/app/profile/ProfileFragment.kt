package com.chesire.nekome.app.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.app.profile.databinding.FragmentProfileBinding
import com.chesire.nekome.core.models.UserModel
import com.chesire.nekome.core.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Fragment to display to the user information about their profile.
 */
@LogLifecykle
@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<ProfileViewModel> { viewModelFactory }
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding not set" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        viewModel.user.observe(viewLifecycleOwner) { userModel ->
            userModel?.let { setImagery(it) }
        }
    }

    private fun setImagery(user: UserModel) {
        binding.profileAvatar.load(user.avatar.largest?.url) {
            transformations(CircleCropTransformation())
        }
    }
}
