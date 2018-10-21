package com.chesire.malime.service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.util.SharedPref
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val SCHEDULER_ID = 138

class RefreshTokenHelper {
    fun schedule(context: Context, sharedPref: SharedPref, force: Boolean = false) {
        if (!isScheduleValid(sharedPref, force)) {
            Timber.v("Refresh token service will not activate")
            return
        }
        sharedPref.setRefreshTokenSchedulerEnabled(true)

        val serviceComponent = ComponentName(context, RefreshTokenService::class.java)
        val builder = JobInfo.Builder(SCHEDULER_ID, serviceComponent)
            .setPeriodic(TimeUnit.HOURS.toMillis(12))
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)

        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
        jobScheduler?.schedule(builder.build())
    }

    fun cancel(context: Context, sharedPref: SharedPref) {
        sharedPref.setRefreshTokenSchedulerEnabled(false)

        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
        jobScheduler?.cancel(SCHEDULER_ID)
    }

    private fun isScheduleValid(sharedPref: SharedPref, force: Boolean): Boolean {
        // If there is no primary service, we haven't logged in yet
        return if (
            sharedPref.getPrimaryService() == SupportedService.Unknown ||
            sharedPref.getForceBlockServices()
        ) {
            false
        } else {
            !(sharedPref.getRefreshTokenSchedulerEnabled() && !force)
        }
    }
}