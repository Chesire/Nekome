package com.chesire.malime.view.login.mal

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import com.chesire.malime.R
import com.chesire.malime.databinding.FragmentMalLoginBinding
import com.chesire.malime.mal.MalManagerFactory
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.login.LoginInteractor
import com.chesire.malime.view.login.LoginStatus
import com.chesire.malime.view.login.LoginViewModelFactory

@Suppress("DEPRECATION")
class MalLoginFragment : Fragment() {
    private lateinit var loginInteractor: LoginInteractor
    private lateinit var loginButton: Button
    private lateinit var viewModel: MalLoginViewModel
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders
            .of(
                this,
                LoginViewModelFactory(
                    requireActivity().application,
                    SharedPref(requireActivity().application),
                    MalManagerFactory()
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

        progressDialog = ProgressDialog(context, R.style.AppTheme_Dark_Dialog).apply {
            isIndeterminate = true
            setMessage(getString(R.string.login_authenticating))
        }
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
            )

        binding.vm = viewModel

        loginButton = binding.loginButton
        loginButton.setOnClickListener {
            executeLoginMethod()
        }
        binding.loginPasswordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                executeLoginMethod()
            }
            false
        }

        return binding.root
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        loginInteractor = context as LoginInteractor
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

    private fun processErrorResponse(@StringRes stringId: Int?) {
        if (stringId != null) {
            Toast.makeText(context, getString(stringId), Toast.LENGTH_LONG).show()
        }
    }

    private fun processLoginResponse(loginStatus: LoginStatus?) {
        if (loginStatus == null) {
            return
        }

        when (loginStatus) {
            LoginStatus.PROCESSING -> {
                loginButton.isEnabled = false
                progressDialog.show()
            }
            LoginStatus.SUCCESS -> {
                loginInteractor.loginSuccessful()
            }
            LoginStatus.FINISHED -> {
                progressDialog.dismiss()
            }
            else -> {
                loginButton.isEnabled = true
            }
        }
    }

    private fun hideSystemKeyboard() {
        requireActivity().currentFocus?.let {
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
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