package com.chesire.nekome.services

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class WorkerQueueTests {

    @Test
    fun `enqueueAuthRefresh enqueues the request on the manager`() {
        val mockWorkManager = mockk<WorkManager> {
            every {
                enqueueUniquePeriodicWork(
                    "AuthSync",
                    ExistingPeriodicWorkPolicy.KEEP,
                    any()
                )
            } returns mockk()
        }

        WorkerQueue(mockWorkManager).run {
            enqueueAuthRefresh()

            verify {
                mockWorkManager.enqueueUniquePeriodicWork(
                    "AuthSync",
                    ExistingPeriodicWorkPolicy.KEEP,
                    any()
                )
            }
        }
    }

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
    fun `enqueueUserRefresh enqueues the request on the manager`() {
        val mockWorkManager = mockk<WorkManager> {
            every {
                enqueueUniquePeriodicWork(
                    "UserSync",
                    ExistingPeriodicWorkPolicy.KEEP,
                    any()
                )
            } returns mockk()
        }

        WorkerQueue(mockWorkManager).run {
            enqueueUserRefresh()

            verify {
                mockWorkManager.enqueueUniquePeriodicWork(
                    "UserSync",
                    ExistingPeriodicWorkPolicy.KEEP,
                    any()
                )
            }
        }
    }
}
