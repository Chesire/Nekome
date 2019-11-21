package com.chesire.malime.core

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles events for authorization.
 */
@Singleton
class AuthCaster @Inject constructor() {
    private val subscriptions = mutableSetOf<AuthCasterListener>()

    /**
     * Subscribe to errors occurring for Auth.
     */
    fun subscribeToAuthError(listener: AuthCasterListener) = subscriptions.add(listener)

    /**
     * Unsubscribe from errors occurring for Auth.
     */
    fun unsubscribeFromAuthError(listener: AuthCasterListener) = subscriptions.remove(listener)

    /**
     * Notify listeners that there was an issue refreshing the auth token.
     */
    fun issueRefreshingToken() = subscriptions.forEach { it.unableToRefresh() }

    /**
     * Listener for when auth events occur.
     */
    interface AuthCasterListener {
        /**
         * Unable to refresh the token, a 401 has occurred.
         * This requires the user logging back in.
         */
        fun unableToRefresh()
    }
}
