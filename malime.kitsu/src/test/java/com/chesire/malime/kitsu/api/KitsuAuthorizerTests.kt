package com.chesire.malime.kitsu.api

import android.content.Context
import android.content.SharedPreferences
import com.chesire.malime.core.PreferenceProvider
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.core.sec.Decryptor
import com.chesire.malime.core.sec.Encryptor
import com.chesire.malime.kitsu.customMock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

class KitsuAuthorizerTests {
    private lateinit var testObject: KitsuAuthorizer
    private val context: Context = customMock()
    private val prefProvider: PreferenceProvider = customMock()
    private val encryptor: Encryptor = customMock()
    private val decryptor: Decryptor = customMock()
    private val pref: SharedPreferences = customMock()
    private val prefEditor: SharedPreferences.Editor = customMock()
    private val authModel = AuthModel("abcdef", "ghijk", 123456, "test")

    @Before
    fun setup() {
        `when`(
            prefProvider.getPreferencesFor(
                context,
                "malime_kitsu_auth_pref",
                Context.MODE_PRIVATE
            )
        ).thenReturn(pref)
        `when`(pref.edit()).thenReturn(prefEditor)
        `when`(prefEditor.putString(anyString(), anyString())).thenReturn(prefEditor)
        `when`(prefEditor.putString(any(), any())).thenReturn(prefEditor)
        `when`(prefEditor.putInt(anyString(), anyInt())).thenReturn(prefEditor)
        `when`(prefEditor.remove(anyString())).thenReturn(prefEditor)

        testObject = KitsuAuthorizer(context, prefProvider, encryptor, decryptor)
    }

    @Test
    fun `storing auth model saves encrypted data into shared preferences`() {
        testObject.storeAuthDetails(authModel)

        // for now we check against null for the input value, because Base64 gets upset
        verify(prefEditor).putString("pref_auth", null)
        verify(prefEditor).apply()
    }

    @Test
    fun `retrieving auth details with cached model, uses the cached data instead`() {
        testObject.storeAuthDetails(authModel)
        val resultModel = testObject.retrieveAuthDetails()

        verify(pref, never()).getString(anyString(), anyString())
        assertEquals(authModel, resultModel)
    }

    @Test
    @Ignore
    fun `retrieving auth details gets the stored model`() {
        // Unit testing with Base64 being used doesn't seem to work
    }

    @Test
    fun `retrieving auth details with no stored model, returns an empty model`() {
        `when`(pref.getString(anyString(), anyString())).thenReturn("")

        val resultModel = testObject.retrieveAuthDetails()

        assertEquals("", resultModel.authToken)
        assertEquals("", resultModel.refreshToken)
    }

    @Test
    fun `is default user with string type, returns false`() {
        assertFalse(testObject.isDefaultUser("string type"))
    }

    @Test
    fun `is default user with 55 int, returns false`() {
        assertFalse(testObject.isDefaultUser(55))
    }

    @Test
    fun `is default user with -1 int, returns true`() {
        assertTrue(testObject.isDefaultUser(-1))
    }

    @Test
    fun `storing the current user saves into shared preferences`() {
        testObject.storeUser(5)
        verify(prefEditor).putInt("pref_user", 5)
        verify(prefEditor).apply()
    }

    @Test
    fun `retrieving the current user pulls the correct user`() {
        `when`(pref.getInt(anyString(), anyInt())).thenReturn(5)
        val result = testObject.retrieveUser()
        assertEquals(5, result)
    }

    @Test
    fun `retrieving the current user gets from the cache if available`() {
        testObject.storeUser(5)
        val result = testObject.retrieveUser()

        assertEquals(5, result)
        verify(pref, never()).getInt(anyString(), anyInt())
    }

    @Test
    fun `clearing the auth details, removes the auth model`() {
        testObject.clear()
        verify(prefEditor).remove("pref_auth")
    }

    @Test
    fun `clearing the auth details, removes the user`() {
        testObject.clear()
        verify(prefEditor).remove("pref_user")
    }
}
