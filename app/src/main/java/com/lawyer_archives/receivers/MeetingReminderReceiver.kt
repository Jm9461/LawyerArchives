package com.lawyer_archives.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.lawyer_archives.R
import com.lawyer_archives.utils.NotificationUtils

class MeetingReminderReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "MEETING_REMINDER_CHANNEL"
        const val NOTIFICATION_ID = 1001
    }

    override fun onReceive(context: Context, intent: Intent) {
        val meetingTitle = intent.getStringExtra("MEETING_TITLE") ?: "Reminder"
        val meetingTime = intent.getStringExtra("MEETING_TIME") ?: "Time unknown"

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Meeting Reminders",
                NotificationManager.IMPORTANCE_HIGH // اصلاح: استفاده از ثابت از NotificationManager
            )
            channel.description = "Reminders for upcoming meetings"
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_meeting)
            .setContentTitle(meetingTitle)
            .setContentText("Meeting starts at $meetingTime")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}