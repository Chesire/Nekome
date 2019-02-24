package com.chesire.malime.kitsu

import android.content.SharedPreferences
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthProviderTests {
    @Test
    fun `accessToken#get returns the decrypted token`() {
        val expected = "accessToken"
        val mockPreferences = mockk<SharedPreferences> {
            every { getString("KEY_ACCESS_TOKEN", "") } returns "token"
        }
        val mockCryption = mockk<Cryption> {
            every { base64Decrypt("token") } returns byteArrayOf()
            every { decryptData(any()) } returns expected
        }

        val classUnderTest = AuthProvider(mockPreferences, mockCryption)

        assertEquals(expected, classUnderTest.accessToken)
    }

    @Test
    fun `accessToken#get with empty token returns empty String`() {
        val expected = ""
        val mockPreferences = mockk<SharedPreferences> {
            every { getString("KEY_ACCESS_TOKEN", "") } returns ""
        }
        val mockCryption = mockk<Cryption> {
            every { base64Decrypt("token") } returns byteArrayOf()
            every { decryptData(any()) } returns expected
        }

        val classUnderTest = AuthProvider(mockPreferences, mockCryption)

        assertEquals(expected, classUnderTest.accessToken)
    }

    @Test
    fun `accessToken#set saves new encrypted accessToken`() {
        val slot = CapturingSlot<String>()
        val expected = "encryptedToken"
        val mockEditor = mockk<SharedPreferences.Editor> {
            every { putString("KEY_ACCESS_TOKEN", capture(slot)) } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }
        val mockCryption = mockk<Cryption> {
            every { base64Encrypt(any()) } returns expected
            every { encryptText("newToken") } returns byteArrayOf()
        }

        val classUnderTest = AuthProvider(mockPreferences, mockCryption)
        classUnderTest.accessToken = "newToken"

        verify { mockEditor.putString("KEY_ACCESS_TOKEN", expected) }
        assertEquals(expected, slot.captured)
    }

    @Test
    fun `refreshToken#get returns the decrypted token`() {
        val expected = "refreshToken"
        val mockPreferences = mockk<SharedPreferences> {
            every { getString("KEY_REFRESH_TOKEN", "") } returns "token"
        }
        val mockCryption = mockk<Cryption> {
            every { base64Decrypt("token") } returns byteArrayOf()
            every { decryptData(any()) } returns expected
        }

        val classUnderTest = AuthProvider(mockPreferences, mockCryption)

        assertEquals(expected, classUnderTest.refreshToken)
    }

    @Test
    fun `refreshToken#get with empty token returns empty String`() {
        val expected = ""
        val mockPreferences = mockk<SharedPreferences> {
            every { getString("KEY_REFRESH_TOKEN", "") } returns ""
        }
        val mockCryption = mockk<Cryption> {
            every { base64Decrypt("token") } returns byteArrayOf()
            every { decryptData(any()) } returns expected
        }

        val classUnderTest = AuthProvider(mockPreferences, mockCryption)

        assertEquals(expected, classUnderTest.refreshToken)
    }

    @Test
    fun `refreshToken#set saves new encrypted refreshToken`() {
        val slot = CapturingSlot<String>()
        val expected = "encryptedToken"
        val mockEditor = mockk<SharedPreferences.Editor> {
            every { putString("KEY_REFRESH_TOKEN", capture(slot)) } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }
        val mockCryption = mockk<Cryption> {
            every { base64Encrypt(any()) } returns expected
            every { encryptText("newToken") } returns byteArrayOf()
        }

        val classUnderTest = AuthProvider(mockPreferences, mockCryption)
        classUnderTest.refreshToken = "newToken"

        verify { mockEditor.putString("KEY_REFRESH_TOKEN", expected) }
        assertEquals(expected, slot.captured)
    }
}
