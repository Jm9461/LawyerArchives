package com.lawyer_archives.utils

import android.content.Context
import com.lawyer_archives.models.CourtSession
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object CalendarUtils {

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(calendar.time)
    }

    fun formatDate(date: String, inputFormat: String, outputFormat: String): String {
        val inputFormatter = SimpleDateFormat(inputFormat, Locale.getDefault())
        val outputFormatter = SimpleDateFormat(outputFormat, Locale.getDefault())
        val dateObj = inputFormatter.parse(date)
        return if (dateObj != null) outputFormatter.format(dateObj) else date
    }

    // اصلاح: حذف چک SDK_INT زیرا minSdk 23 است
    // قبل از اصلاح، این خط وجود داشت:
    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    //     cal.isWeekend = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
    // }
    // حالا که minSdk 23 است، این ویژگی همیشه در دسترس است.
    fun isWeekend(date: Long): Boolean {
        val cal = Calendar.getInstance()
        cal.timeInMillis = date
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
    }
}