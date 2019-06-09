package com.chesire.malime.flow.login.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R
import com.chesire.malime.databinding.FragmentDetailsBinding
import com.chesire.malime.extensions.hide
import com.chesire.malime.extensions.hideSystemKeyboard
import com.chesire.malime.extensions.show
import com.chesire.malime.flow.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_details.fragmentDetailsLayout
import kotlinx.android.synthetic.main.fragment_details.fragmentDetailsLoginButton
import kotlinx.android.synthetic.main.fragment_details.fragmentDetailsPasswordLayout
import kotlinx.android.synthetic.main.fragment_details.fragmentDetailsPasswordText
import kotlinx.android.synthetic.main.fragment_details.fragmentDetailsProgressBar
import kotlinx.android.synthetic.main.fragment_details.fragmentDetailsUsernameLayout
import kotlinx.android.synthetic.main.fragment_details.fragmentDetailsUsernameText
import timber.log.Timber
import javax.inject.Inject

@LogLifecykle
class DetailsFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(DetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDetailsBinding
        .inflate(inflater, container, false)
        .apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        .root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fragmentDetailsUsernameText.addTextChangedListener {
            fragmentDetailsUsernameLayout.error = ""
        }
        fragmentDetailsPasswordText.addTextChangedListener {
            fragmentDetailsPasswordLayout.error = ""
        }
        fragmentDetailsPasswordText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                activity?.hideSystemKeyboard()
                if (viewModel.loginStatus.value != LoginStatus.Loading) {
                    viewModel.login()
                    return@setOnEditorActionListener true
                }
            }

            false
        }
        viewModel.loginStatus.observe(viewLifecycleOwner, Observer { loginStatusChanged(it) })
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
                fragmentDetailsUsernameLayout.error = getString(R.string.login_error_empty_username)
            LoginStatus.EmptyPassword ->
                fragmentDetailsPasswordLayout.error = getString(R.string.login_error_empty_password)
            LoginStatus.Error -> showSnackbar(R.string.login_error_generic)
            LoginStatus.InvalidCredentials -> showSnackbar(R.string.login_error_credentials)
            LoginStatus.Success ->
                findNavController().navigate(DetailsFragmentDirections.toSyncingFragment())
            else -> Timber.w("Unexpected LoginStatus [$loginStatus]")
        }
    }

    private fun showSnackbar(@StringRes id: Int) =
        Snackbar.make(fragmentDetailsLayout, id, Snackbar.LENGTH_LONG).show()

    private fun setLoading() {
        fragmentDetailsProgressBar.show()
        fragmentDetailsLoginButton.isEnabled = false
    }

    private fun hideLoading() {
        fragmentDetailsProgressBar.hide(true)
        fragmentDetailsLoginButton.isEnabled = true
    }
}
