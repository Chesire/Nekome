package com.chesire.malime.view.login.mal

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.chesire.malime.R
import com.chesire.malime.databinding.FragmentMalLoginBinding
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.login.BaseLoginFragment
import com.chesire.malime.view.login.LoginViewModelFactory

@Suppress("DEPRECATION")
class MalLoginFragment : BaseLoginFragment() {
    private lateinit var viewModel: MalLoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders
            .of(
                this,
                LoginViewModelFactory(
                    requireActivity().application,
                    SharedPref(requireActivity().application)
                )
            )
            .get(MalLoginViewModel::class.java)

        viewModel.loginResponse.observe(
            this,
            Observer {
                processLoginResponse(it)
            }
        )
        viewModel.errorResponse.observe(
            this,
            Observer {
                processErrorResponse(it)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            DataBindingUtil.inflate<FragmentMalLoginBinding>(
                inflater,
                R.layout.fragment_mal_login,
                container,
                false
            ).apply {
                vm = viewModel
                loginButton.setOnClickListener { executeLoginMethod() }
                loginPasswordEditText.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        executeLoginMethod()
                    }
                    false
                }
            }

        return binding.root
    }

    private fun executeLoginMethod() {
        hideSystemKeyboard()

        // We have to convert to base64 here, or the unit tests won't work as Base64 is an Android class
        viewModel.executeLogin(
            Base64.encodeToString(
                "${viewModel.loginModel.userName}:${viewModel.loginModel.password}".toByteArray(
                    Charsets.UTF_8
                ), Base64.NO_WRAP
            )
        )
    }

    companion object {
        const val tag = "MalLoginFragment"
        fun newInstance(): MalLoginFragment {
            val loginFragment = MalLoginFragment()
            val args = Bundle()
            loginFragment.arguments = args
            return loginFragment
        }
    }
}