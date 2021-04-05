package com.chesire.nekome

import com.chesire.nekome.database.RoomDB
import com.chesire.nekome.datasource.auth.AccessTokenRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.Test

class LogoutHandlerTests {

    @Test
    fun `executeLogout clears db`() {
        val mockAccessTokenRepository = mockk<AccessTokenRepository> {
            every { clear() } just Runs
        }
        val mockDb = mockk<RoomDB> {
            every { clearAllTables() } just Runs
        }

        LogoutHandler(mockAccessTokenRepository, mockDb).run {
            executeLogout()
        }

        verifyAll { mockDb.clearAllTables() }
    }

    @Test
    fun `executeLogout clears authProvider`() {
        val mockAccessTokenRepository = mockk<AccessTokenRepository> {
            every { clear() } just Runs
        }
        val mockDb = mockk<RoomDB> {
            every { clearAllTables() } just Runs
        }

        LogoutHandler(mockAccessTokenRepository, mockDb).run {
            executeLogout()
        }

        verifyAll { mockAccessTokenRepository.clear() }
    }
}
