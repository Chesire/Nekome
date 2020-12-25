package com.chesire.nekome.app.login.details

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chesire.nekome.app.login.R
import com.chesire.nekome.app.login.databinding.FragmentDetailsBinding
import com.chesire.nekome.core.extensions.hide
import com.chesire.nekome.core.extensions.hideSystemKeyboard
import com.chesire.nekome.core.extensions.setLinkedText
import com.chesire.nekome.core.extensions.show
import com.chesire.nekome.core.url.UrlHandler
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * Fragment to allow the user to enter their login details for Kitsu.
 */
@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    @Inject
    lateinit var urlHandler: UrlHandler

    private val viewModel by viewModels<DetailsViewModel>()
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding not set" }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailsBinding.bind(view)

        binding.usernameText.addTextChangedListener { binding.usernameLayout.error = "" }
        binding.passwordText.addTextChangedListener { binding.passwordLayout.error = "" }
        binding.passwordText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                activity?.hideSystemKeyboard()
                if (viewModel.loginStatus.value != LoginStatus.Loading) {
                    executeLogin()
                    return@setOnEditorActionListener true
                }
            }

            false
        }
        binding.loginButton.setOnClickListener { executeLogin() }

        setupLinks()
        viewModel.loginStatus.observe(viewLifecycleOwner) { loginStatusChanged(it) }
    }

    private fun setupLinks() {
        binding.signUp.setLinkedText(R.string.login_sign_up, R.string.login_sign_up_link_target) {
            urlHandler.launch(requireContext(), getString(R.string.login_sign_up_url))
        }

        binding.forgotPasswordButton.setOnClickListener {
            urlHandler.launch(requireContext(), getString(R.string.login_forgot_password_url))
        }
    }

    private fun executeLogin() {
        viewModel.login(binding.usernameText.text.toString(), binding.passwordText.text.toString())
    }

    private fun loginStatusChanged(loginStatus: LoginStatus) {
        Timber.i("LoginStatus updated with value [$loginStatus]")
        if (loginStatus == LoginStatus.Loading) {
            setLoading()
            return
        } else {
            hideLoading()
        }

        when (loginStatus) {
            LoginStatus.EmptyUsername ->
                binding.usernameLayout.error = getString(R.string.login_error_empty_username)
            LoginStatus.EmptyPassword ->
                binding.passwordLayout.error = getString(R.string.login_error_empty_password)
            LoginStatus.Error -> showSnackbar(R.string.login_error_generic)
            LoginStatus.InvalidCredentials -> showSnackbar(R.string.login_error_credentials)
            LoginStatus.Success -> {
                activity?.hideSystemKeyboard()
                findNavController().navigate(DetailsFragmentDirections.toSyncingFragment())
            }
            else -> Timber.w("Unexpected LoginStatus [$loginStatus]")
        }
    }

    private fun showSnackbar(@StringRes id: Int) =
        Snackbar.make(binding.detailsLayout, id, Snackbar.LENGTH_LONG).show()

    private fun setLoading() {
        binding.progressBar.show()
        binding.loginButton.text = ""
        binding.loginButton.isEnabled = false
    }

    private fun hideLoading() {
        binding.progressBar.hide(true)
        binding.loginButton.text = getString(R.string.login_login)
        binding.loginButton.isEnabled = true
    }
}
