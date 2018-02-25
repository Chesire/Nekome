package com.chesire.malime

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.chesire.malime.mal.MalManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginActivity : AppCompatActivity() {

    // Some dump account details created to test this
    private var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.login_button).setOnClickListener { executeLoginMethod() }
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
        val malManager = MalManager(
                findViewById<EditText>(R.id.login_username_edit_text).text.toString(),
                findViewById<EditText>(R.id.login_password_edit_text).text.toString()
        )

        disposables.add(malManager.loginToAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { _ ->
                            // we need to also store the credentials used
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        },
                        { _ ->
                            Snackbar.make(findViewById(R.id.login_layout), R.string.login_failure, Snackbar.LENGTH_LONG)
                                    .show()
                        }
                ))
    }
}
