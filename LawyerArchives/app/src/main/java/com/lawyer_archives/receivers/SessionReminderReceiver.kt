package com.lawyer_archives.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lawyer_archives.utils.NotificationUtils

class SessionReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("sessionTitle") ?: "جلسه"
        val date = intent.getStringExtra("sessionDate") ?: "تاریخ نامشخص"
        NotificationUtils.showSessionReminder(context, title, date)
    }
}