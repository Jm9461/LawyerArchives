package com.lawyer_archives.utils

import java.text.SimpleDateFormat
import java.util.*

object CalendarUtils {
    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun parseDate(dateString: String): Date? {
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return try {
            sdf.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }
}