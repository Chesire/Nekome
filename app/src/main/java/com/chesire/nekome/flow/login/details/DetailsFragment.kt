package com.chesire.nekome.flow.login.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.R
import com.chesire.nekome.core.extensions.hide
import com.chesire.nekome.core.extensions.hideSystemKeyboard
import com.chesire.nekome.core.extensions.show
import com.chesire.nekome.core.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_details.detailsLayout
import kotlinx.android.synthetic.main.fragment_details.detailsLoginButton
import kotlinx.android.synthetic.main.fragment_details.detailsPasswordLayout
import kotlinx.android.synthetic.main.fragment_details.detailsPasswordText
import kotlinx.android.synthetic.main.fragment_details.detailsProgressBar
import kotlinx.android.synthetic.main.fragment_details.detailsUsernameLayout
import kotlinx.android.synthetic.main.fragment_details.detailsUsernameText
import timber.log.Timber
import javax.inject.Inject

/**
 * Fragment to allow the user to enter their login details for Kitsu.
 */
@LogLifecykle
class DetailsFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get<DetailsViewModel>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsUsernameText.addTextChangedListener {
            detailsUsernameLayout.error = ""
        }
        detailsPasswordText.addTextChangedListener {
            detailsPasswordLayout.error = ""
        }
        detailsPasswordText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                activity?.hideSystemKeyboard()
                if (viewModel.loginStatus.value != LoginStatus.Loading) {
                    executeLogin()
                    return@setOnEditorActionListener true
                }
            }

            false
        }
        detailsLoginButton.setOnClickListener { executeLogin() }
        viewModel.loginStatus.observe(viewLifecycleOwner, Observer { loginStatusChanged(it) })
    }

    private fun executeLogin() {
        viewModel.login(detailsUsernameText.text.toString(), detailsPasswordText.text.toString())
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
            LoginStatus.EmptyUsername -> detailsUsernameLayout.error =
                getString(R.string.login_error_empty_username)
            LoginStatus.EmptyPassword -> detailsPasswordLayout.error =
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
        Snackbar.make(detailsLayout, id, Snackbar.LENGTH_LONG).show()

    private fun setLoading() {
        detailsProgressBar.show()
        detailsLoginButton.text = ""
        detailsLoginButton.isEnabled = false
    }

    private fun hideLoading() {
        detailsProgressBar.hide(true)
        detailsLoginButton.text = getString(R.string.login_login)
        detailsLoginButton.isEnabled = true
    }
}
