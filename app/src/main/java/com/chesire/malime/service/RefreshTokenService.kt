package com.chesire.malime.service

import android.app.job.JobParameters
import android.app.job.JobService
import com.chesire.malime.kitsu.api.KitsuAuthorizer
import com.chesire.malime.kitsu.api.KitsuManager
import dagger.android.AndroidInjection
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RefreshTokenService : JobService() {
    // currently just Kitsu, when others are supported this should be updated
    @Inject
    lateinit var kitsuManager: KitsuManager
    @Inject
    lateinit var kitsuAuthorizer: KitsuAuthorizer

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onStopJob(params: JobParameters?) = false

    override fun onStartJob(params: JobParameters?): Boolean {
        Timber.v("Refresh token service primed, updating auth token")

        val currentAuthModel = kitsuAuthorizer.retrieveAuthDetails()
        if (currentAuthModel.expireAt == 0L || currentAuthModel.refreshToken.isEmpty()) {
            Timber.v("ExpireAt is set to 0 or no refresh token detected")
            return true
        }

        val expireAt = currentAuthModel.expireAt
        val current = System.currentTimeMillis() / 1000
        val diff = (expireAt - current) * 1000

        // If there are less than two days left, refresh token
        if (TimeUnit.MILLISECONDS.toDays(diff) < 2) {
            refreshAuthTokens(params, currentAuthModel.refreshToken)
        } else {
            Timber.v("No need to refresh token yet, expiry in [$diff]")
        }
        return true
    }

    private fun refreshAuthTokens(params: JobParameters?, refreshToken: String) {
        kitsuManager.getNewAuthToken(refreshToken)
            .subscribeOn(Schedulers.computation())
            .subscribe({
                Timber.d("Refresh token service has received new auth token")
                kitsuAuthorizer.storeAuthDetails(it)
            }, {
                Timber.e("Refresh token service encountered an issue")
                jobFinished(params, true)
            })
    }
}