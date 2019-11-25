package com.chesire.nekome.testing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * *Test Only*
 * Rule to use during tests that forces all coroutines to execute on the [TestCoroutineDispatcher].
 */
@ExperimentalCoroutinesApi
class CoroutinesMainDispatcherRule : TestWatcher() {
    /**
     * The dispatcher used during tests, accessible in order to inject the same dispatcher that the
     * rest of the test might use.
     */
    val testDispatcher = TestCoroutineDispatcher()

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}
