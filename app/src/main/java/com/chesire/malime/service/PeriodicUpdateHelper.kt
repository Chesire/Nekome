package com.chesire.malime.service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.chesire.malime.util.SharedPref
import timber.log.Timber
import java.util.concurrent.TimeUnit

class PeriodicUpdateHelper {
    private val schedulerId = 137

    fun schedule(context: Context, sharedPref: SharedPref) {
        if (sharedPref.getSeriesUpdateSchedulerEnabled()) {
            Timber.v("Scheduler is already running")
            return
        }
        sharedPref.setSeriesUpdateSchedulerEnabled(true)

        val serviceComponent = ComponentName(context, PeriodicUpdateService::class.java)
        val builder = JobInfo.Builder(schedulerId, serviceComponent)
            .setPeriodic(TimeUnit.HOURS.toMillis(12))
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)

        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
        jobScheduler?.schedule(builder.build())
    }

    fun cancel(context: Context, sharedPref: SharedPref) {
        sharedPref.setSeriesUpdateSchedulerEnabled(false)

        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
        jobScheduler?.cancel(schedulerId)
    }
}