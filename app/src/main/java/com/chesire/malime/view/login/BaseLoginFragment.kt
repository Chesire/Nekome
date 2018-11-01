package com.chesire.malime.view.login

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import com.chesire.malime.R
import dagger.android.support.DaggerFragment

@Suppress("DEPRECATION")
abstract class BaseLoginFragment : DaggerFragment() {
    private lateinit var loginInteractor: LoginInteractor
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressDialog = ProgressDialog(context, R.style.AppTheme_Dark_Dialog).apply {
            isIndeterminate = true
            setMessage(getString(R.string.login_authenticating))
        }
    }

    @Suppress("UnsafeCast")
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        loginInteractor = context as LoginInteractor
    }

    protected fun processLoginResponse(loginStatus: LoginStatus?) {
        if (loginStatus == null) {
            return
        }

        when (loginStatus) {
            LoginStatus.PROCESSING -> progressDialog.show()
            LoginStatus.SUCCESS -> loginInteractor.loginSuccessful()
            LoginStatus.FINISHED -> progressDialog.dismiss()
            LoginStatus.ERROR -> { // Handled in the view model
            }
        }
    }

    protected fun processErrorResponse(@StringRes stringId: Int?) {
        if (stringId != null) {
            Toast.makeText(context, getString(stringId), Toast.LENGTH_LONG).show()
        }
    }
}
