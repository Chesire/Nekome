package com.chesire.malime.view.login

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import com.chesire.malime.R
import com.chesire.malime.databinding.ActivityLoginBinding
import com.chesire.malime.mal.MalManagerFactory
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.MainActivity

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var viewModel: LoginViewModel
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        viewModel = ViewModelProviders
            .of(
                this,
                LoginViewModelFactory(
                    application,
                    SharedPref(applicationContext),
                    MalManagerFactory()
                )
            )
            .get(LoginViewModel::class.java)

        binding.vm = viewModel

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

        supportActionBar?.hide()
        actionBar?.hide()

        progressDialog = ProgressDialog(this, R.style.AppTheme_Dark_Dialog).apply {
            isIndeterminate = true
            setMessage(getString(R.string.login_authenticating))
        }

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
            Toast.makeText(this, getString(stringId), Toast.LENGTH_LONG).show()
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
                startActivity(Intent(this, MainActivity::class.java))
                finish()
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
        currentFocus?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}
