package com.chesire.malime

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.chesire.malime.mal.MalManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    // Some dump account details created to test this
    private val malManager by lazy { MalManager() }
    private var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        executeTestMethod()
    }

    override fun onResume() {
        super.onResume()
        disposables = CompositeDisposable()
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    fun executeTestMethod() {
        disposables.add(malManager.searchForAnime("Naruto")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { entries ->
                            Timber.i("Success")
                        },
                        { error ->
                            Timber.e("Error")
                        }
                ))
    }
}
