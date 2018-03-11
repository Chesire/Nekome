package com.chesire.malime

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import com.chesire.malime.mal.MalManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginActivity : AppCompatActivity() {

    private var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.login_button).setOnClickListener { executeLoginMethod() }
        findViewById<EditText>(R.id.login_password_edit_text).setOnEditorActionListener { _, actionId, _ ->
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
        val username = findViewById<EditText>(R.id.login_username_edit_text).text.toString()
        val password = findViewById<EditText>(R.id.login_password_edit_text).text.toString()

        // Username must be 2-16 chars long, we can add validation to this later
        if (username.isBlank() || password.isBlank()) {
            return
        }

        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.isEnabled = false

        val b64: String = Base64.encodeToString("$username:$password".toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
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
                        },
                        { _ ->
                            Snackbar.make(findViewById(R.id.login_layout), R.string.login_failure, Snackbar.LENGTH_LONG)
                                    .show()
                            loginButton.isEnabled = true
                        }
                ))
    }
}
