package com.chesire.nekome.core

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class AuthCasterTests {
    @Test
    fun `subscribeToAuthError adds listener that gets callbacks`() {
        val mockListener = mockk<AuthCaster.AuthCasterListener> {
            every { unableToRefresh() } just Runs
        }

        AuthCaster().run {
            subscribeToAuthError(mockListener)
            issueRefreshingToken()
        }

        verify { mockListener.unableToRefresh() }
    }

    @Test
    fun `unsubscribeFromAuthError removes listener from getting callbacks`() {
        val mockListener = mockk<AuthCaster.AuthCasterListener> {
            every { unableToRefresh() } just Runs
        }

        AuthCaster().run {
            subscribeToAuthError(mockListener)
            issueRefreshingToken()
            unsubscribeFromAuthError(mockListener)
            issueRefreshingToken()
        }

        verify(exactly = 1) { mockListener.unableToRefresh() }
    }

    @Test
    fun `issueRefreshingToken notifies all listeners`() {
        val mockListener1 = mockk<AuthCaster.AuthCasterListener> {
            every { unableToRefresh() } just Runs
        }
        val mockListener2 = mockk<AuthCaster.AuthCasterListener> {
            every { unableToRefresh() } just Runs
        }
        val mockListener3 = mockk<AuthCaster.AuthCasterListener> {
            every { unableToRefresh() } just Runs
        }

        AuthCaster().run {
            subscribeToAuthError(mockListener1)
            subscribeToAuthError(mockListener2)
            subscribeToAuthError(mockListener3)
            issueRefreshingToken()
        }

        verify {
            mockListener1.unableToRefresh()
            mockListener2.unableToRefresh()
            mockListener3.unableToRefresh()
        }
    }
}
