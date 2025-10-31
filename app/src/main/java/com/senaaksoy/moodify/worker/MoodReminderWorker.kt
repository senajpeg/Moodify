package com.senaaksoy.moodify.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.senaaksoy.moodify.MainActivity
import com.senaaksoy.moodify.R
import com.senaaksoy.moodify.repository.DeezerRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class MoodReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val deezerRepository: DeezerRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val notificationType = inputData.getString(KEY_NOTIFICATION_TYPE) ?: return Result.failure()

        return try {
            showNotification(notificationType)
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private suspend fun showNotification(type: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(notificationManager)

        val (title, message, emoji) = when (type) {
            TYPE_MORNING -> Triple(
                applicationContext.getString(R.string.notification_morning_title),
                applicationContext.getString(R.string.notification_morning_message),
                "🎶"
            )
            TYPE_AFTERNOON -> Triple(
                applicationContext.getString(R.string.notification_afternoon_title),
                applicationContext.getString(R.string.notification_afternoon_message),
                "🌈"
            )
            TYPE_EVENING -> Triple(
                applicationContext.getString(R.string.notification_evening_title),
                applicationContext.getString(R.string.notification_evening_message),
                "🌙"
            )
            TYPE_WEEKLY -> {
                val analysis = try {
                    deezerRepository.getWeeklyMoodAnalysis()
                } catch (e: Exception) {
                    applicationContext.getString(R.string.notification_weekly_message_default)
                }
                Triple(
                    applicationContext.getString(R.string.notification_weekly_title),
                    analysis,
                    "😊"
                )
            }
            else -> return
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            type.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("$emoji $title")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(type.hashCode(), notification)
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                applicationContext.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description =
                    applicationContext.getString(R.string.notification_channel_description)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "moodify_reminders"
        const val KEY_NOTIFICATION_TYPE = "notification_type"

        const val TYPE_MORNING = "morning"
        const val TYPE_AFTERNOON = "afternoon"
        const val TYPE_EVENING = "evening"
        const val TYPE_WEEKLY = "weekly"
    }
}
