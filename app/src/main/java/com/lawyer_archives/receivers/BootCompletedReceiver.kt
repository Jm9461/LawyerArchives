package com.lawyer_archives.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lawyer_archives.utils.XmlManager

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // در حال حاضر فقط سشن‌ها را لود می‌کنیم
            // متد scheduleSessionAlarm در CalendarUtils وجود ندارد، بنابراین کامنت شده
            val sessions = XmlManager.loadSessions(context)

            // TODO: بعداً می‌توانید آلارم‌ها را مجدداً تنظیم کنید
            // for (session in sessions) {
            //     if (!session.isCompleted) {
            //         CalendarUtils.scheduleSessionAlarm(context, session)
            //     }
            // }
        }
    }
}