package com.chesire.malime.view.login.kitsu

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.chesire.malime.R
import com.chesire.malime.databinding.FragmentKitsuLoginBinding
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.login.BaseLoginFragment
import com.chesire.malime.view.login.LoginViewModelFactory

@Suppress("DEPRECATION")
class KitsuLoginFragment : BaseLoginFragment() {
    private lateinit var viewModel: KitsuLoginViewModel

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
            .get(KitsuLoginViewModel::class.java)

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
            DataBindingUtil.inflate<FragmentKitsuLoginBinding>(
                inflater,
                R.layout.fragment_kitsu_login,
                container,
                false
            )

        binding.vm = viewModel

        binding.loginButton.setOnClickListener {
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

    private fun executeLoginMethod() {
        hideSystemKeyboard()
        viewModel.executeLogin()
    }


    companion object {
        const val tag = "KitsuLoginFragment"
        fun newInstance(): KitsuLoginFragment {
            val kitsuLoginFragment = KitsuLoginFragment()
            val args = Bundle()
            kitsuLoginFragment.arguments = args
            return kitsuLoginFragment
        }
    }
}