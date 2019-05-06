package com.chesire.malime

import com.chesire.malime.db.RoomDB
import com.chesire.malime.kitsu.AuthProvider
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
