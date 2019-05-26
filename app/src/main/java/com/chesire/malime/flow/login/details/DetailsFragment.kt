package com.chesire.malime.flow.login.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R
import com.chesire.malime.databinding.FragmentDetailsBinding
import com.chesire.malime.flow.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_details.fragmentDetailsLayout
import kotlinx.android.synthetic.main.fragment_details.fragmentDetailsPasswordLayout
import kotlinx.android.synthetic.main.fragment_details.fragmentDetailsPasswordText
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
    ): View {
        return FragmentDetailsBinding
            .inflate(inflater, container, false)
            .apply {
                vm = viewModel
                lifecycleOwner = viewLifecycleOwner
            }
            .root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fragmentDetailsUsernameText.addTextChangedListener {
            fragmentDetailsUsernameLayout.error = ""
        }
        fragmentDetailsPasswordText.addTextChangedListener {
            fragmentDetailsPasswordLayout.error = ""
        }
        viewModel.loginStatus.observe(viewLifecycleOwner, Observer { loginStatusChanged(it) })
    }

    private fun loginStatusChanged(loginStatus: LoginStatus) {
        Timber.i("LoginStatus updated with value [$loginStatus]")
        when (loginStatus) {
            LoginStatus.EmptyUsername ->
                fragmentDetailsUsernameLayout.error = getString(R.string.login_error_empty_username)
            LoginStatus.EmptyPassword ->
                fragmentDetailsPasswordLayout.error = getString(R.string.login_error_empty_password)
            LoginStatus.Error -> Snackbar.make(
                fragmentDetailsLayout,
                R.string.login_error_generic,
                Snackbar.LENGTH_LONG
            ).show()
            LoginStatus.InvalidCredentials -> Snackbar.make(
                fragmentDetailsLayout,
                R.string.login_error_credentials,
                Snackbar.LENGTH_LONG
            ).show()
            LoginStatus.Success ->
                findNavController().navigate(DetailsFragmentDirections.toSyncingFragment())
        }
    }
}
