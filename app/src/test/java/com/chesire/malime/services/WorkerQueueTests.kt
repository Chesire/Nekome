package com.chesire.malime.services

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class WorkerQueueTests {
    @Test
    fun `enqueueSeriesRefresh enqueues the request on the manager`() {
        val mockWorkManager = mockk<WorkManager> {
            every {
                enqueueUniquePeriodicWork(
                    "SeriesSync",
                    ExistingPeriodicWorkPolicy.KEEP,
                    any()
                )
            } returns mockk()
        }

        WorkerQueue(mockWorkManager).run {
            enqueueSeriesRefresh()

            verify {
                mockWorkManager.enqueueUniquePeriodicWork(
                    "SeriesSync",
                    ExistingPeriodicWorkPolicy.KEEP,
                    any()
                )
            }
        }
    }

    @Test
    fun `cancelEnqueued cancels the SeriesRefresh worker`() {
        val mockWorkManager = mockk<WorkManager> {
            every { cancelUniqueWork("SeriesSync") } returns mockk()
        }

        WorkerQueue(mockWorkManager).run {
            cancelQueued()
            verify { mockWorkManager.cancelUniqueWork("SeriesSync") }
        }
    }
}
