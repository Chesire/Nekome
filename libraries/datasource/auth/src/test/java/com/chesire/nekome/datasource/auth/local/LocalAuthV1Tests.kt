package com.chesire.nekome.datasource.auth.local

import android.content.SharedPreferences
import com.chesire.nekome.encryption.Cryption
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private const val KITSU_ACCESS_TOKEN_KEY = "KEY_KITSU_ACCESS_TOKEN"
private const val KITSU_REFRESH_TOKEN_KEY = "KEY_KITSU_REFRESH_TOKEN"

class LocalAuthV1Tests {

    @Test
    fun `hasCredentials returns true if access token exists`() {
        val mockPreferences = mockk<SharedPreferences> {
            every { contains(KITSU_ACCESS_TOKEN_KEY) } returns true
            every { contains(KITSU_REFRESH_TOKEN_KEY) } returns false
        }
        val mockCryption = mockk<Cryption>()

        val classUnderTest = LocalAuthV1(mockPreferences, mockCryption)

        assertTrue(classUnderTest.hasCredentials)
    }

    @Test
    fun `hasCredentials returns true if refresh token exists`() {
        val mockPreferences = mockk<SharedPreferences> {
            every { contains(KITSU_ACCESS_TOKEN_KEY) } returns false
            every { contains(KITSU_REFRESH_TOKEN_KEY) } returns true
        }
        val mockCryption = mockk<Cryption>()

        val classUnderTest = LocalAuthV1(mockPreferences, mockCryption)

        assertTrue(classUnderTest.hasCredentials)
    }

    @Test
    fun `hasCredentials returns true if both tokens exist`() {
        val mockPreferences = mockk<SharedPreferences> {
            every { contains(KITSU_ACCESS_TOKEN_KEY) } returns true
            every { contains(KITSU_REFRESH_TOKEN_KEY) } returns true
        }
        val mockCryption = mockk<Cryption>()

        val classUnderTest = LocalAuthV1(mockPreferences, mockCryption)

        assertTrue(classUnderTest.hasCredentials)
    }

    @Test
    fun `hasCredentials returns false if neither tokens exist`() {
        val mockPreferences = mockk<SharedPreferences> {
            every { contains(KITSU_ACCESS_TOKEN_KEY) } returns false
            every { contains(KITSU_REFRESH_TOKEN_KEY) } returns false
        }
        val mockCryption = mockk<Cryption>()

        val classUnderTest = LocalAuthV1(mockPreferences, mockCryption)

        assertFalse(classUnderTest.hasCredentials)
    }

    @Test
    fun `accessToken#get returns the decrypted token`() {
        val expected = "accessToken"
        val mockPreferences = mockk<SharedPreferences> {
            every { getString(KITSU_ACCESS_TOKEN_KEY, null) } returns "token"
        }
        val mockCryption = mockk<Cryption> {
            every { decrypt("token", any()) } returns expected
        }

        val classUnderTest = LocalAuthV1(mockPreferences, mockCryption)

        assertEquals(expected, classUnderTest.accessToken)
    }

    @Test
    fun `accessToken#get with empty token returns empty String`() {
        val expected = ""
        val mockPreferences = mockk<SharedPreferences> {
            every { getString(KITSU_ACCESS_TOKEN_KEY, null) } returns null
        }
        val mockCryption = mockk<Cryption> {
            every { decrypt("", any()) } returns expected
        }

        val classUnderTest = LocalAuthV1(mockPreferences, mockCryption)

        assertEquals(expected, classUnderTest.accessToken)
    }

    @Test
    fun `accessToken#set saves new encrypted accessToken`() {
        val slot = CapturingSlot<String>()
        val expected = "encryptedToken"
        val mockEditor = mockk<SharedPreferences.Editor> {
            every { putString(KITSU_ACCESS_TOKEN_KEY, capture(slot)) } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }
        val mockCryption = mockk<Cryption> {
            every { encrypt("newToken", any()) } returns expected
        }

        val classUnderTest = LocalAuthV1(mockPreferences, mockCryption)
        classUnderTest.accessToken = "newToken"

        verify { mockEditor.putString(KITSU_ACCESS_TOKEN_KEY, expected) }
        assertEquals(expected, slot.captured)
    }

    @Test
    fun `refreshToken#get returns the decrypted token`() {
        val expected = "refreshToken"
        val mockPreferences = mockk<SharedPreferences> {
            every { getString(KITSU_REFRESH_TOKEN_KEY, null) } returns "token"
        }
        val mockCryption = mockk<Cryption> {
            every { decrypt("token", any()) } returns expected
        }

        val classUnderTest = LocalAuthV1(mockPreferences, mockCryption)

        assertEquals(expected, classUnderTest.refreshToken)
    }

    @Test
    fun `refreshToken#get with empty token returns empty String`() {
        val expected = ""
        val mockPreferences = mockk<SharedPreferences> {
            every { getString(KITSU_REFRESH_TOKEN_KEY, null) } returns null
        }
        val mockCryption = mockk<Cryption> {
            every { decrypt("", any()) } returns expected
        }

        val classUnderTest = LocalAuthV1(mockPreferences, mockCryption)

        assertEquals(expected, classUnderTest.refreshToken)
    }

    @Test
    fun `refreshToken#set saves new encrypted refreshToken`() {
        val slot = CapturingSlot<String>()
        val expected = "encryptedToken"
        val mockEditor = mockk<SharedPreferences.Editor> {
            every { putString(KITSU_REFRESH_TOKEN_KEY, capture(slot)) } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }
        val mockCryption = mockk<Cryption> {
            every { encrypt("newToken", any()) } returns expected
        }

        val classUnderTest = LocalAuthV1(mockPreferences, mockCryption)
        classUnderTest.refreshToken = "newToken"

        verify { mockEditor.putString(KITSU_REFRESH_TOKEN_KEY, expected) }
        assertEquals(expected, slot.captured)
    }

    @Test
    fun `clear clears access token`() {
        val mockEditor = mockk<SharedPreferences.Editor> {
            every { remove(KITSU_ACCESS_TOKEN_KEY) } returns this
            every { remove(KITSU_REFRESH_TOKEN_KEY) } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }
        val mockCryption = mockk<Cryption>()

        val classUnderTest = LocalAuthV1(mockPreferences, mockCryption)
        classUnderTest.clear()

        verify { mockEditor.remove(KITSU_ACCESS_TOKEN_KEY) }
    }

    @Test
    fun `clear clears refresh token`() {
        val mockEditor = mockk<SharedPreferences.Editor> {
            every { remove(KITSU_ACCESS_TOKEN_KEY) } returns this
            every { remove(KITSU_REFRESH_TOKEN_KEY) } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }
        val mockCryption = mockk<Cryption>()

        val classUnderTest = LocalAuthV1(mockPreferences, mockCryption)
        classUnderTest.clear()

        verify { mockEditor.remove(KITSU_REFRESH_TOKEN_KEY) }
    }
}
