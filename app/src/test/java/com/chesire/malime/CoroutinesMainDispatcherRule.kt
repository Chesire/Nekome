package com.chesire.malime

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class CoroutinesMainDispatcherRule : TestWatcher() {
    private val singleThreadExecutor = Executors.newSingleThreadExecutor()

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(Dispatchers.Unconfined)
        // Medium article says not to set this to Unconfined, but without this it looks like it
        // won't be on the correct thread, so will leave like this for now
        // Dispatchers.setMain(singleThreadExecutor.asCoroutineDispatcher())
    }

    override fun finished(description: Description?) {
        super.finished(description)
        // singleThreadExecutor.shutdownNow()
        Dispatchers.resetMain()
    }
}
