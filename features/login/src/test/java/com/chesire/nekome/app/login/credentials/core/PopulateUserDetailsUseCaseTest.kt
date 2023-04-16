@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.login.credentials.core

import com.chesire.nekome.datasource.user.UserRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PopulateUserDetailsUseCaseTest {

    private val user = mockk<UserRepository>()
    private lateinit var populateUserDetails: PopulateUserDetailsUseCase

    @Before
    fun setup() {
        clearAllMocks()
        populateUserDetails = PopulateUserDetailsUseCase(user)
    }

    @Test
    fun `Given populate user succeeds, Then return result of Ok`() = runTest {
        coEvery { user.refreshUser() } returns Resource.Success(Unit)

        val result = populateUserDetails()

        assertTrue(result is Ok)
    }

    @Test
    fun `Given populate user fails, Then return result of Err`() = runTest {
        coEvery { user.refreshUser() } returns Resource.Error("")

        val result = populateUserDetails()

        assertTrue(result is Err)
    }
}
