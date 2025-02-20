package com.problemsolver.event.sync

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.problemsolver.event.sync.ReminderWorker.Companion.WORK_NAME
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReminderScheduler @Inject constructor(
    private val workManager: WorkManager
) {
    fun scheduleReminder(interval: Long = 1, timeUnit: TimeUnit = TimeUnit.DAYS) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(interval, timeUnit)
            .setConstraints(constraints)
            .setInitialDelay(5, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    fun cancelReminder() {
        workManager.cancelUniqueWork(WORK_NAME)
    }
}