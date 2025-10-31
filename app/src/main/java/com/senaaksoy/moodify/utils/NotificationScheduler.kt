package com.senaaksoy.moodify.utils

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.senaaksoy.moodify.R
import com.senaaksoy.moodify.worker.MoodReminderWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    fun scheduleAllNotifications(context: Context) {
        scheduleMorningReminder(context)
        scheduleAfternoonReminder(context)
        scheduleEveningReminder(context)
        scheduleWeeklyReminder(context)
    }

    private fun scheduleMorningReminder(context: Context) {
        scheduleNotification(
            context = context,
            hour = 9,
            minute = 0,
            type = MoodReminderWorker.TYPE_MORNING,
            workName = context.getString(R.string.work_name_morning)
        )
    }

    private fun scheduleAfternoonReminder(context: Context) {
        scheduleNotification(
            context = context,
            hour = 13,
            minute = 0,
            type = MoodReminderWorker.TYPE_AFTERNOON,
            workName = context.getString(R.string.work_name_afternoon)
        )
    }

    private fun scheduleEveningReminder(context: Context) {
        scheduleNotification(
            context = context,
            hour = 20,
            minute = 0,
            type = MoodReminderWorker.TYPE_EVENING,
            workName = context.getString(R.string.work_name_evening)
        )
    }

    private fun scheduleWeeklyReminder(context: Context) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)

            // Eğer bu pazar geçmişse, bir sonraki pazara ayarla
            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        val delay = calendar.timeInMillis - System.currentTimeMillis()

        val inputData = Data.Builder()
            .putString(MoodReminderWorker.KEY_NOTIFICATION_TYPE, MoodReminderWorker.TYPE_WEEKLY)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<MoodReminderWorker>(
            repeatInterval = 7,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            context.getString(R.string.work_name_weekly),
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    private fun scheduleNotification(
        context: Context,
        hour: Int,
        minute: Int,
        type: String,
        workName: String
    ) {
        val currentTime = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)

            // Eğer hedef saat geçmişse, bir sonraki güne ayarla
            if (before(currentTime)) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val delay = targetTime.timeInMillis - currentTime.timeInMillis

        val inputData = Data.Builder()
            .putString(MoodReminderWorker.KEY_NOTIFICATION_TYPE, type)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<MoodReminderWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            workName,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    fun cancelAllNotifications(context: Context) {
        WorkManager.getInstance(context).apply {
            cancelUniqueWork(context.getString(R.string.work_name_morning))
            cancelUniqueWork(context.getString(R.string.work_name_afternoon))
            cancelUniqueWork(context.getString(R.string.work_name_evening))
            cancelUniqueWork(context.getString(R.string.work_name_weekly))
        }
    }
}
