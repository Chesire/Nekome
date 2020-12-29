package com.chesire.nekome.app.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.chesire.nekome.account.User
import com.chesire.nekome.account.UserDomain
import com.chesire.nekome.app.profile.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to display to the user information about their profile.
 */
@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel by viewModels<ProfileViewModel>()
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
            userModel?.let { user ->
                if (user is User.Found) {
                    setImagery(user.domain)
                }
            }
        }
    }

    private fun setImagery(user: UserDomain) {
        binding.profileAvatar.load(user.avatar.largest?.url) {
            transformations(CircleCropTransformation())
        }
    }
}
