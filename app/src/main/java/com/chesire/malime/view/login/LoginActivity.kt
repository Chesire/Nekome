package com.chesire.malime.view.login

import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.chesire.malime.R
import com.chesire.malime.databinding.ActivityLoginBinding
import com.chesire.malime.mal.MalManager
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class LoginActivity : AppCompatActivity() {

    private var disposables = CompositeDisposable()
    private lateinit var loginButton: Button
    private lateinit var usernameText: EditText
    private lateinit var passwordText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        val model = ViewModelProviders
            .of(this, LoginViewModelFactory(application))
            .get(LoginViewModel::class.java)

        binding.vm = model

        supportActionBar?.hide()
        actionBar?.hide()

        loginButton = findViewById(R.id.login_button)
        usernameText = findViewById(R.id.login_username_edit_text)
        passwordText = findViewById(R.id.login_password_edit_text)

        loginButton.setOnClickListener { executeLoginMethod() }
        passwordText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                executeLoginMethod()
            }
            false
        }
    }

    override fun onResume() {
        super.onResume()
        disposables = CompositeDisposable()
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    private fun executeLoginMethod() {
        hideSystemKeyboard()

        val username = usernameText.text.toString()
        val password = passwordText.text.toString()

        if (!isValid(username, password)) {
            return
        }

        loginButton.isEnabled = false

        // We use the progress dialog here, because the user doesn't have anything else to do on login anyway
        @Suppress("DEPRECATION")
        val progressDialog = ProgressDialog(this, R.style.AppTheme_Dark_Dialog).apply {
            isIndeterminate = true
            setMessage(getString(R.string.login_authenticating))
            show()
        }

        val b64 =
            Base64.encodeToString("$username:$password".toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
        val malManager = MalManager(b64)

        disposables.add(malManager.loginToAccount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _ ->
                    val sharedPref = SharedPref(this)
                    sharedPref.putUsername(username).putAuth(b64)

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                    progressDialog.dismiss()
                },
                { _ ->
                    progressDialog.dismiss()
                    loginFailure(getString(R.string.login_failure))
                }
            ))
    }

    private fun hideSystemKeyboard() {
        currentFocus?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun isValid(username: String, password: String): Boolean {
        return when {
            username.isBlank() -> {
                loginFailure(getString(R.string.login_failure_username))
                false
            }
            password.isBlank() -> {
                loginFailure(getString(R.string.login_failure_password))
                false
            }
            else -> true
        }
    }

    private fun loginFailure(reason: String) {
        Toast.makeText(this, reason, Toast.LENGTH_LONG).show()
        loginButton.isEnabled = true
    }
}
