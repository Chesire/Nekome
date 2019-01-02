package com.chesire.malime.view.login.kitsu

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.chesire.malime.R
import com.chesire.malime.databinding.FragmentKitsuLoginBinding
import com.chesire.malime.util.autoCleared
import com.chesire.malime.util.extension.hideSystemKeyboard
import com.chesire.malime.view.login.BaseLoginFragment
import javax.inject.Inject

private const val KITSU_SIGNUP_URL = "https://kitsu.io/explore/anime"

class KitsuLoginFragment : BaseLoginFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: KitsuLoginViewModel
    private var binding by autoCleared<FragmentKitsuLoginBinding>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders
            .of(this, viewModelFactory)
            .get(KitsuLoginViewModel::class.java)
            .apply {
                loginResponse.observe(
                    viewLifecycleOwner,
                    Observer {
                        processLoginResponse(it)
                    }
                )
                errorResponse.observe(
                    viewLifecycleOwner,
                    Observer {
                        processErrorResponse(it)
                    }
                )
            }
        binding.vm = viewModel
        binding.setLifecycleOwner(viewLifecycleOwner)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil
            .inflate<FragmentKitsuLoginBinding>(
                inflater,
                R.layout.fragment_kitsu_login,
                container,
                false
            ).apply {
                binding = this
                fragmentKitsuLoginButton.setOnClickListener { executeLoginMethod() }
                fragmentKitsuLoginCreateAccountText.setOnClickListener { createAccount() }
                fragmentKitsuLoginPasswordEditText.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        executeLoginMethod()
                    }
                    false
                }
            }.root
    }

    private fun createAccount() {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(context, Uri.parse(KITSU_SIGNUP_URL))
    }

    private fun executeLoginMethod() {
        requireActivity().hideSystemKeyboard(requireContext())
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
