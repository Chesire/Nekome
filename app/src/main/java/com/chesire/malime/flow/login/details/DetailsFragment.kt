package com.chesire.malime.flow.login.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.databinding.FragmentDetailsBinding
import com.chesire.malime.flow.ViewModelFactory
import dagger.android.support.DaggerFragment
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

        viewModel.loginStatus.observe(
            viewLifecycleOwner,
            Observer { loginStatus ->
                when (loginStatus) {
                    LoginStatus.EmptyUsername -> Timber.i("LoginStatus returned empty username")
                    LoginStatus.EmptyPassword -> Timber.i("LoginStatus returned empty password")
                    LoginStatus.Error -> Timber.i("LoginStatus returned error")
                    LoginStatus.Success -> {
                        Timber.i("LoginStatus returned success")
                        findNavController().navigate(DetailsFragmentDirections.toSyncingFragment())
                    }
                    null -> Timber.w("LoginStatus returned as null")
                }
            }
        )
    }
}
