package com.chesire.malime.view.login

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.widget.Toast
import com.chesire.malime.R

@Suppress("DEPRECATION")
abstract class BaseLoginFragment : Fragment() {
    private lateinit var loginInteractor: LoginInteractor
    private lateinit var progressDialog: ProgressDialog

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

    protected fun processLoginResponse(loginStatus: LoginStatus?) {
        if (loginStatus == null) {
            return
        }

        when (loginStatus) {
            LoginStatus.PROCESSING -> {
                progressDialog.show()
            }
            LoginStatus.SUCCESS -> {
                loginInteractor.loginSuccessful()
            }
            LoginStatus.FINISHED -> {
                progressDialog.dismiss()
            }
            LoginStatus.ERROR -> {
                // Handled in the view model
            }
        }
    }

    protected fun processErrorResponse(@StringRes stringId: Int?) {
        if (stringId != null) {
            Toast.makeText(context, getString(stringId), Toast.LENGTH_LONG).show()
        }
    }
}