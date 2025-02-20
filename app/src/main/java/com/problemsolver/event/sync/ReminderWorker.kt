package com.problemsolver.event.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.problemsolver.event.data.source.local.dataStore
import com.problemsolver.event.utils.AppNotification
import com.problemsolver.event.utils.AppNotification.NOTIFICATION_ID
import com.problemsolver.event.utils.formatDateTime
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val WORK_NAME = "ReminderWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            val event = context.dataStore.data.first().upcomingEvent
            AppNotification.showNotification(context, event.name, formatDateTime(event.beginTime))
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val event = context.dataStore.data.first().upcomingEvent
        return ForegroundInfo(
            NOTIFICATION_ID,
            AppNotification.createNotification(context, event.name, formatDateTime(event.beginTime)).build()
        )
    }
}