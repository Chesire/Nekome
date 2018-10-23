package com.chesire.malime.service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.util.SharedPref
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val SCHEDULER_ID = 137

class PeriodicUpdateHelper {
    fun schedule(context: Context, sharedPref: SharedPref, force: Boolean = false) {
        if (!isScheduleValid(sharedPref, force)) {
            Timber.v("Periodic update service will not activate")
            return
        }
        sharedPref.seriesUpdateSchedulerEnabled = true

        val serviceComponent = ComponentName(context, PeriodicUpdateService::class.java)
        val builder = JobInfo.Builder(SCHEDULER_ID, serviceComponent)
            .setPeriodic(TimeUnit.HOURS.toMillis(12))
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)

        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
        jobScheduler?.schedule(builder.build())
    }

    fun cancel(context: Context, sharedPref: SharedPref) {
        sharedPref.seriesUpdateSchedulerEnabled = false

        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
        jobScheduler?.cancel(SCHEDULER_ID)
    }

    private fun isScheduleValid(sharedPref: SharedPref, force: Boolean): Boolean {
        // If there is no primary service, we haven't logged in yet
        return if (
            sharedPref.primaryService == SupportedService.Unknown ||
            sharedPref.getForceBlockServices()
        ) {
            false
        } else {
            !(sharedPref.seriesUpdateSchedulerEnabled && !force)
        }
    }
}