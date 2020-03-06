package com.chesire.nekome.flow.login.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.R
import com.chesire.nekome.core.extensions.hide
import com.chesire.nekome.core.extensions.hideSystemKeyboard
import com.chesire.nekome.core.extensions.setLinkedText
import com.chesire.nekome.core.extensions.show
import com.chesire.nekome.core.url.UrlHandler
import com.chesire.nekome.core.viewmodel.ViewModelFactory
import com.chesire.nekome.databinding.FragmentDetailsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

/**
 * Fragment to allow the user to enter their login details for Kitsu.
 */
@LogLifecykle
class DetailsFragment : DaggerFragment() {
    @Inject
    lateinit var urlHandler: UrlHandler

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<DetailsViewModel> { viewModelFactory }
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding not set" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDetailsBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        viewModel.loginStatus.observe(viewLifecycleOwner, Observer { loginStatusChanged(it) })
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
            LoginStatus.EmptyUsername -> binding.usernameLayout.error =
                getString(R.string.login_error_empty_username)
            LoginStatus.EmptyPassword -> binding.passwordLayout.error =
                getString(R.string.login_error_empty_password)
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
