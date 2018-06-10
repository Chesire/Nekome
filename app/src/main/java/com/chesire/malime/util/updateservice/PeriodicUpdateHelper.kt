package com.chesire.malime.util.updateservice

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import java.util.concurrent.TimeUnit

// TODO: This should be moved into Core
class PeriodicUpdateHelper {
    private val schedulerId = 137

    fun schedule(context: Context) {
        val serviceComponent = ComponentName(context, PeriodicUpdateService::class.java)
        val builder = JobInfo.Builder(schedulerId, serviceComponent)
            .setPeriodic(TimeUnit.HOURS.toMillis(12))
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)

        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(builder.build())
    }

    fun cancel(context: Context) {
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancel(schedulerId)
    }
}