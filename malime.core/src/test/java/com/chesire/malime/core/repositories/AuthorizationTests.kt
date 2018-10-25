package com.chesire.malime.core.repositories

import com.chesire.malime.core.api.Authorizer
import com.chesire.malime.core.flags.SupportedService
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class AuthorizationTests {
    private val mockStringAuthorizer: Authorizer<String> = mock { }
    private val mockIntAuthorizer: Authorizer<Int> = mock { }

    @Test
    fun `no authorizers returns false for hasLoggedIn`() {
        assertFalse(Authorization(mapOf()).hasLoggedIn())
    }

    @Test
    fun `no logged in accounts, returns false for hasLoggedIn`() {
        `when`(mockStringAuthorizer.isDefaultUser(any())).thenReturn(true)
        `when`(mockIntAuthorizer.isDefaultUser(any())).thenReturn(true)

        val testObject = Authorization(
            mapOf(
                SupportedService.Unknown to mockStringAuthorizer,
                SupportedService.Kitsu to mockIntAuthorizer
            )
        )

        assertFalse(testObject.hasLoggedIn())
    }

    @Test
    fun `an account logged in, returns true for hasLoggedIn`() {
        `when`(mockStringAuthorizer.isDefaultUser(any())).thenReturn(false)
        `when`(mockIntAuthorizer.isDefaultUser(any())).thenReturn(true)

        val testObject = Authorization(
            mapOf(
                SupportedService.Unknown to mockStringAuthorizer,
                SupportedService.Kitsu to mockIntAuthorizer
            )
        )

        assertTrue(testObject.hasLoggedIn())
    }

    @Test
    fun `all accounts logged in, returns true for hasLoggedIn`() {
        `when`(mockStringAuthorizer.isDefaultUser(any())).thenReturn(false)
        `when`(mockIntAuthorizer.isDefaultUser(any())).thenReturn(false)

        val testObject = Authorization(
            mapOf(
                SupportedService.Unknown to mockStringAuthorizer,
                SupportedService.Kitsu to mockIntAuthorizer
            )
        )

        assertTrue(testObject.hasLoggedIn())
    }

    @Test
    fun `attempting to getUser Int with no authorizers, returns null`() {
        val testObject = Authorization(mapOf())
        val res = testObject.getUser<Int>(SupportedService.Unknown)

        assertNull(res)
    }

    @Test
    fun `attempting to getUser String with no authorizers, returns null`() {
        val testObject = Authorization(mapOf())
        val res = testObject.getUser<String>(SupportedService.Unknown)

        assertNull(res)
    }

    @Test
    fun `attempting to getUser with wrong type, returns null`() {
        val testObject = Authorization(
            mapOf(
                SupportedService.Unknown to mockStringAuthorizer
            )
        )
        val res = testObject.getUser<Authorization>(SupportedService.Unknown)

        assertNull(res)
    }

    @Test
    fun `attempting to getUser with single authorizer, returns expected type`() {
        val expected = "test"
        `when`(mockStringAuthorizer.retrieveUser()).thenReturn(expected)

        val testObject = Authorization(
            mapOf(
                SupportedService.Unknown to mockStringAuthorizer
            )
        )

        assertEquals(expected, testObject.getUser<String>(SupportedService.Unknown))
    }

    @Test
    fun `attempting to getUser with multiple authorizers, returns expected one`() {
        val expectedString = "test"
        val expectedInt = 6
        `when`(mockStringAuthorizer.retrieveUser()).thenReturn(expectedString)
        `when`(mockIntAuthorizer.retrieveUser()).thenReturn(expectedInt)

        val testObject = Authorization(
            mapOf(
                SupportedService.Unknown to mockStringAuthorizer,
                SupportedService.Kitsu to mockIntAuthorizer
            )
        )

        assertEquals(expectedString, testObject.getUser<String>(SupportedService.Unknown))
        assertEquals(expectedInt, testObject.getUser<Int>(SupportedService.Kitsu))
    }

    @Test
    fun `attempting to getUser with multiple same type authorizers, returns expected one`() {
        val expectedString1 = "test1"
        val expectedString2 = "test2"
        val mockStringAuth1: Authorizer<String> = mock {
            on { retrieveUser() }.thenReturn(expectedString1)
        }
        val mockStringAuth2: Authorizer<String> = mock {
            on { retrieveUser() }.thenReturn(expectedString2)
        }

        val testObject = Authorization(
            mapOf(
                SupportedService.Unknown to mockStringAuth1,
                SupportedService.Kitsu to mockStringAuth2
            )
        )

        assertEquals(expectedString1, testObject.getUser<String>(SupportedService.Unknown))
        assertEquals(expectedString2, testObject.getUser<String>(SupportedService.Kitsu))
    }

    @Test
    fun `attempting to logout of all authorizers with no authorizers doesn't error`() {
        val testObject = Authorization(mapOf())
        testObject.logoutAll()
    }

    @Test
    fun `attempting to logout of all authorizers with one authorizer logs out`() {
        val testObject = Authorization(mapOf(SupportedService.Unknown to mockStringAuthorizer))
        testObject.logoutAll()
        verify(mockStringAuthorizer).clear()
    }

    @Test
    fun `attempting to logout of all authorizers with multiple authorizers logs out`() {
        val testObject = Authorization(
            mapOf(
                SupportedService.Unknown to mockStringAuthorizer,
                SupportedService.Kitsu to mockIntAuthorizer
            )
        )
        testObject.logoutAll()

        verify(mockStringAuthorizer).clear()
        verify(mockIntAuthorizer).clear()
    }
}
