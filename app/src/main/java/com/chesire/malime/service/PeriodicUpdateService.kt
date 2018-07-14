package com.chesire.malime.service

import android.app.job.JobParameters
import android.app.job.JobService
import com.chesire.malime.core.repositories.Library
import dagger.android.AndroidInjection
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class PeriodicUpdateService : JobService() {
    @Inject
    lateinit var library: Library

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onStopJob(params: JobParameters?) = false

    override fun onStartJob(params: JobParameters?): Boolean {
        Timber.v("Periodic update service primed, updating libraries")
        getLatestLibrary(params, library)
        return true
    }

    private fun getLatestLibrary(params: JobParameters?, library: Library) {
        library.updateLibraryFromApi()
            .subscribeOn(Schedulers.computation())
            .subscribe({
                Timber.d("Periodic update service has received new library")
                library.insertIntoLocalLibrary(it)
            }, {
                Timber.e(it, "Periodic update service encountered an issue getting new library")
                jobFinished(params, true)
            })
    }
}