package com.chesire.nekome

import com.chesire.nekome.database.RoomDB
import com.chesire.nekome.datasource.auth.AccessTokenRepository
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LogoutHandlerTests {

    private val accessTokenRepository = mockk<AccessTokenRepository>(relaxed = true)
    private val roomDB = mockk<RoomDB>(relaxed = true)
    private lateinit var logoutHandler: LogoutHandler

    @Before
    fun setup() {
        clearAllMocks()

        logoutHandler = LogoutHandler(accessTokenRepository, roomDB)
    }

    @Test
    fun `executeLogout clears db`() = runTest {
        logoutHandler.executeLogout()

        coVerify { roomDB.clearAllTables() }
    }

    @Test
    fun `executeLogout clears authProvider`() = runTest {
        logoutHandler.executeLogout()

        coVerify { accessTokenRepository.clear() }
    }
}
