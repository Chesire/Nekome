package com.chesire.malime.util.updateservice

import android.app.job.JobParameters
import android.app.job.JobService
import com.chesire.malime.core.repositories.Library
import com.chesire.malime.util.SharedPref
import dagger.android.AndroidInjection
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class PeriodicUpdateService : JobService() {
    @Inject
    lateinit var library: Library
    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Timber.i("UpdateService primed, updating libraries")
        getLatestLibrary(params, library)
        return true
    }

    private fun getLatestLibrary(params: JobParameters?, library: Library) {
        library.updateLibraryFromApi()
            .subscribeOn(Schedulers.computation())
            .subscribe({
                Timber.d("Periodic update has received new library")
                library.insertIntoLocalLibrary(it)
            }, {
                Timber.e(it, "Periodic update encountered an issue getting new library")
                jobFinished(params, true)
            })
    }
}