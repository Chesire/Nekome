package com.chesire.malime

import com.chesire.malime.kitsu.AuthProvider
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogoutHandler @Inject constructor(
    private val authProvider: AuthProvider
) : AuthCaster.AuthCasterListener {

    /**
     * Subscribe to auth issues that are fired from the [AuthCaster].
     */
    fun subscribe() = AuthCaster.subscribeToAuthError(this)

    /**
     * Unsubscribe from auth issues that are fired from the [AuthCaster].
     */
    fun unsubscribe() = AuthCaster.unsubscribeFromAuthError(this)

    override fun unableToRefresh() {
        Timber.w("unableToRefresh firing")
        // perform log out method
    }
}
