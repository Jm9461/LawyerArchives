package com.lawyer_archives.utils

import saman.zamani.persiandate.PersianDate
import java.util.*

object DateConverter {

    /**
     * تاریخ جاری شمسی را برمی‌گرداند
     */
    fun getCurrentPersianDate(): String {
        val persianDate = PersianDate()
        return String.format(
            Locale.getDefault(),
            "%d/%02d/%02d",
            persianDate.shYear,
            persianDate.shMonth,
            persianDate.shDay
        )
    }

    /**
     * فرمت کردن زمان
     */
    fun formatTime(hour: Int, minute: Int): String {
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
    }

    /**
     * پارس کردن تاریخ شمسی
     */
    fun parsePersianDate(dateString: String): PersianDate? {
        return try {
            val parts = dateString.split("/")
            if (parts.size == 3) {
                val pd = PersianDate()
                pd.initJalaliDate(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
                pd
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * بررسی اعتبار تاریخ شمسی
     */
    fun isValidPersianDate(dateString: String): Boolean {
        return try {
            val parts = dateString.split("/")
            if (parts.size == 3) {
                val pd = PersianDate()
                pd.initJalaliDate(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * دریافت تاریخ و زمان ترکیبی شمسی
     */
    fun getPersianDateTime(date: String, time: String): String {
        return if (date.isNotEmpty() && time.isNotEmpty()) {
            "$date $time"
        } else {
            ""
        }
    }

    /**
     * تبدیل تاریخ شمسی به متن نمایشی
     */
    fun formatPersianDate(year: Int, month: Int, day: Int): String {
        return String.format(Locale.getDefault(), "%04d/%02d/%02d", year, month, day)
    }

    /**
     * دریافت تاریخ شمسی فعلی به صورت آبجکت
     */
    fun getTodayPersianDate(): PersianDate {
        return PersianDate()
    }

    /**
     * تبدیل Date میلادی به تاریخ شمسی
     */
    fun convertGregorianToPersian(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        
        // ایجاد PersianDate از تاریخ میلادی
        val persianDate = PersianDate(calendar.timeInMillis)
        
        return String.format(
            Locale.getDefault(),
            "%d/%02d/%02d",
            persianDate.shYear,
            persianDate.shMonth,
            persianDate.shDay
        )
    }
}