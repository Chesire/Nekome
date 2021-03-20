package com.chesire.nekome

import com.chesire.nekome.database.RoomDB
import com.chesire.nekome.kitsu.AuthProvider
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.Test

class LogoutHandlerTests {

    @Test
    fun `executeLogout clears db`() {
        val mockAuthProvider = mockk<AuthProvider> {
            every { clearAuth() } just Runs
        }
        val mockDb = mockk<RoomDB> {
            every { clearAllTables() } just Runs
        }

        LogoutHandler(mockAuthProvider, mockDb).run {
            executeLogout()
        }

        verifyAll { mockDb.clearAllTables() }
    }

    @Test
    fun `executeLogout clears authProvider`() {
        val mockAuthProvider = mockk<AuthProvider> {
            every { clearAuth() } just Runs
        }
        val mockDb = mockk<RoomDB> {
            every { clearAllTables() } just Runs
        }

        LogoutHandler(mockAuthProvider, mockDb).run {
            executeLogout()
        }

        verifyAll { mockAuthProvider.clearAuth() }
    }
}
