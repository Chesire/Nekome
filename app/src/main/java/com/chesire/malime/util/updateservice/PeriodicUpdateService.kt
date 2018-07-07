package com.chesire.malime.util.updateservice

import android.app.job.JobParameters
import android.app.job.JobService
import com.chesire.malime.core.api.MalimeApi
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.repositories.Library
import com.chesire.malime.core.room.MalimeDatabase
import com.chesire.malime.kitsu.api.KitsuAuthorizer
import com.chesire.malime.kitsu.api.KitsuManagerFactory
import com.chesire.malime.mal.api.MalAuthorizer
import com.chesire.malime.mal.api.MalManagerFactory
import com.chesire.malime.util.SharedPref
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PeriodicUpdateService : JobService() {
    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        val sharedPref = SharedPref(applicationContext)

        val api: MalimeApi = if (sharedPref.getPrimaryService() == SupportedService.Kitsu) {
            Timber.i("Found Kitsu as supported service")
            KitsuManagerFactory().get(KitsuAuthorizer(applicationContext))
        } else {
            Timber.i("Found Mal as supported service")
            MalManagerFactory().get(MalAuthorizer(applicationContext))
        }

        val library = Library(api, MalimeDatabase.getInstance(applicationContext).malimeDao())

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