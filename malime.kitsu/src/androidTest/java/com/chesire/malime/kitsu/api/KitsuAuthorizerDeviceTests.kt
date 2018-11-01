package com.chesire.malime.kitsu.api

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.chesire.malime.core.PreferenceProvider
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.core.sec.Decryptor
import com.chesire.malime.core.sec.Encryptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class KitsuAuthorizerDeviceTests {
    private val appContext = InstrumentationRegistry.getTargetContext()

    @Test
    fun canRetrieveStoredAuth() {
        val expectedModel = AuthModel("AuthToken0", "RefreshToken0", 100, "Provider0")
        val testObject =
            KitsuAuthorizer(appContext, PreferenceProvider(), Encryptor(appContext), Decryptor())

        assertNotEquals(expectedModel, testObject.retrieveAuthDetails())
        testObject.storeAuthDetails(expectedModel)
        assertEquals(expectedModel, testObject.retrieveAuthDetails())
    }

    @Test
    fun canRetrieveStoredUser() {
        val expectedUser = 5
        val testObject =
            KitsuAuthorizer(appContext, PreferenceProvider(), Encryptor(appContext), Decryptor())

        assertFalse(testObject.retrieveUser() == expectedUser)
        testObject.storeUser(expectedUser)
        assertTrue(testObject.retrieveUser() == expectedUser)
    }

    @Test
    fun retrievingAuthAfterClearReturnsEmptyModel() {
        val storedModel = AuthModel("AuthToken1", "RefreshToken1", 101, "Provider1")
        val testObject =
            KitsuAuthorizer(appContext, PreferenceProvider(), Encryptor(appContext), Decryptor())

        testObject.storeAuthDetails(storedModel)
        assertEquals(storedModel, testObject.retrieveAuthDetails())

        testObject.clear()

        val clearedModel = testObject.retrieveAuthDetails()
        assertTrue(clearedModel.authToken.isBlank())
        assertTrue(clearedModel.refreshToken.isBlank())
    }

    @Test
    fun retrievingUserAfterClearReturnsDefaultUser() {
        val storedUser = 6
        val testObject =
            KitsuAuthorizer(appContext, PreferenceProvider(), Encryptor(appContext), Decryptor())

        testObject.storeUser(storedUser)
        assertEquals(storedUser, testObject.retrieveUser())

        testObject.clear()

        val clearedUser = testObject.retrieveUser()
        assertTrue(testObject.isDefaultUser(clearedUser))
    }
}
