package com.chesire.malime.view.login

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.chesire.malime.R

abstract class BaseLoginFragment : Fragment() {
    protected lateinit var loginInteractor: LoginInteractor
    protected lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressDialog = ProgressDialog(context, R.style.AppTheme_Dark_Dialog).apply {
            isIndeterminate = true
            setMessage(getString(R.string.login_authenticating))
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        loginInteractor = context as LoginInteractor
    }

    protected fun processErrorResponse(@StringRes stringId: Int?) {
        if (stringId != null) {
            Toast.makeText(context, getString(stringId), Toast.LENGTH_LONG).show()
        }
    }

    protected fun hideSystemKeyboard() {
        requireActivity().currentFocus?.let {
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}