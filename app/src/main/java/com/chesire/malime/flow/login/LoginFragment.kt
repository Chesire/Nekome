package com.chesire.malime.flow.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.chesire.malime.databinding.FragmentLoginBinding
import com.chesire.malime.flow.ViewModelFactory
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var loginListener: LoginListener

    private val viewModel: LoginViewModel by lazy {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(LoginViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context !is LoginListener) {
            throw ClassCastException("Activity must implement LoginListener")
        }
        loginListener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentLoginBinding
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
                    LoginViewModel.LoginStatus.EmptyUsername -> Timber.i("LoginStatus returned empty username")
                    LoginViewModel.LoginStatus.EmptyPassword -> Timber.i("LoginStatus returned empty password")
                    LoginViewModel.LoginStatus.Error -> Timber.i("LoginStatus returned error")
                    LoginViewModel.LoginStatus.Success -> {
                        Timber.i("LoginStatus returned success")
                        loginListener.onLoginSuccess()
                    }
                    null -> Timber.w("LoginStatus returned as null")
                }
            }
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment LoginFragment.
         */
        fun newInstance() = LoginFragment()
    }

    interface LoginListener {
        fun onLoginSuccess()
    }
}
