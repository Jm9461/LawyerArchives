package com.lawyer_archives.models

import saman.zamani.persiandate.PersianDate

data class Meeting(
    val id: String,
    val clientName: String,
    val date: String, // تاریخ شمسی - فرمت: 1403/10/15
    val title: String,
    val time: String, // زمان - فرمت: 14:30
    val description: String,
    val reminderOption: String,
    val addedDate: String // تاریخ شمسی
) {
    // constructor قدیمی برای سازگاری با کدهای موجود
    constructor(
        id: String,
        clientName: String,
        date: String,
        title: String,
        time: String,
        description: String,
        addedDate: String
    ) : this(id, clientName, date, title, time, description, "no_reminder", addedDate)

    /**
     * بررسی می‌کند که آیا تاریخ معتبر شمسی است
     */
    fun hasValidPersianDate(): Boolean {
        return date.isNotEmpty()
    }

    /**
     * تاریخ و زمان ترکیبی
     */
    fun getFullDateTime(): String {
        return if (date.isNotEmpty() && time.isNotEmpty()) {
            "$date $time"
        } else {
            ""
        }
    }
}